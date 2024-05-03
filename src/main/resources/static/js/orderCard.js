document.addEventListener("DOMContentLoaded", function() {
    const cards = document.querySelectorAll("#orderCard");

    cards.forEach(card => {
        const status = card.dataset.status;

        if (status === 'IN_PROGRESS') {
            card.classList.add('bg-warning');
        } else if (status === 'ON_THE_WAY') {
            card.classList.add('bg-info');
        } else if (status === 'COMPLETED') {
            card.classList.add('bg-success');
        }
    });
});
