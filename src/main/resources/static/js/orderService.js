document.addEventListener("DOMContentLoaded", function() {
    const payButton = document.querySelector('.btn.btn-success');
    
    payButton.addEventListener('click', function() {
        createSpinnerOnTheButton();

        sessionStorage.setItem("clearCart", "true");

        // Pobierz wartość pola 'orderNotes'
        const orderNotesTextarea = document.querySelector('textarea[name="orderNotes"]');
        const orderNotes = orderNotesTextarea.value;

        //zamienić na JSONa
        document.getElementById('orderNotes').value = orderNotes;

        setTimeout(function() {
            const form = document.getElementById('fakePayForm');
            form.submit();
        }, 3500);

        function createSpinnerOnTheButton() {
            payButton.innerHTML = '';
            payButton.classList.add("button-clicked");
            const spinner = document.createElement('div');
            spinner.classList.add('spinner-border', 'text-primary');
            spinner.setAttribute('role', 'status');
            const span = document.createElement('span');
            span.classList.add('visually-hidden');
            span.textContent = 'Loading...';
            spinner.appendChild(span);
            payButton.appendChild(spinner);
        }
    });
});