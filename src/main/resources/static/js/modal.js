<script>
  // Przechwytujemy kliknięcie przycisku otwierającego modal
  const modalButton = document.querySelector('[data-bs-target="#updateImageModal"]');
  modalButton.addEventListener('click', function() {
  // Pobieramy wartość z atrybutu data-value
  const valueToPass = this.getAttribute('data-value');
  // Ustawiamy tę wartość wewnątrz modala
  document.getElementById('modalValue').innerText = valueToPass;
});
</script>
