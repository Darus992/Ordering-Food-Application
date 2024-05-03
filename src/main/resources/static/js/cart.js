(function() {
    class Cart {
        constructor(cartRequest, totalPrice) {
            this.cartRequest = cartRequest;
            this.totalPrice = totalPrice;
        }
    }

    // Eksportuj instancję klasy lub funkcję, jeśli jest to potrzebne
    window.Cart = Cart;
})();