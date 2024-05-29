package pl.dariuszgilewicz.infrastructure.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class ImageConverterTest {

    @Test
    void convertFileToBytes_shouldReturnByteArray_whenValidMultipartFile() throws IOException {
        // given
        String fileName = "test.txt";
        byte[] content = "Test content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(fileName, content);

        // when
        byte[] result = ImageConverter.convertFileToBytes(multipartFile);

        // then
        assertNotNull(result);
        assertArrayEquals(content, result);
    }

    @Test
    void convertFileToBytes_shouldThrowRuntimeException_whenIOExceptionOccurs() throws IOException {
        // given
        String fileName = "test.txt";
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        doThrow(new IOException("IO error")).when(multipartFile).getBytes();

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ImageConverter.convertFileToBytes(multipartFile);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("An error occurred while converting the file[test.txt] to a byte array."));
    }

    @Test
    void convertFromBytes_shouldReturnCorrectBase64String_whenValidByteArray() {
        // given
        String originalString = "Test string";
        byte[] byteArray = originalString.getBytes();
        String expectedBase64String = Base64.getEncoder().encodeToString(byteArray);

        // when
        String result = ImageConverter.convertFromBytes(byteArray);

        // then
        assertEquals(expectedBase64String, result);
    }

    @ParameterizedTest
    @NullSource
    void convertFromBytes_shouldThrowRuntimeException_whenByteArrayIsNull(byte[] nullByteArray) {
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ImageConverter.convertFromBytes(nullByteArray);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("An error occurred while converting the byte array[null] to a string."));
    }

    @Test
    void convertToBytes_shouldReturnCorrectByteArray_whenValidBase64String() {
        // given
        String originalString = "Test string";
        String base64String = Base64.getEncoder().encodeToString(originalString.getBytes());
        byte[] expectedBytes = originalString.getBytes();

        // when
        byte[] result = ImageConverter.convertToBytes(base64String);

        // then
        assertArrayEquals(expectedBytes, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid_base64_string", "12345", "!!!@@@"})
    void convertToBytes_shouldThrowRuntimeException_whenInvalidBase64String(String invalidBase64String) {
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ImageConverter.convertToBytes(invalidBase64String);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("An error occurred while converting string:[ %s ] to a byte array.".formatted(invalidBase64String)));
    }
}