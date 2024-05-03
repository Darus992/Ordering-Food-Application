(function() {
    class Food {
        constructor(foodId, category, name, description, price) {
            this.foodId = foodId;
            this.category = category;
            this.name = name;
            this.description = description;
            this.price = price;
        }

        equals(other) {
            return this.foodId === other.foodId &&
            this.category === other.category &&
            this.name === other.name &&
            this.description === other.description &&
            this.price === other.price;
        }

        hashCode() {
            return `${this.foodId}_${this.category}_${this.name}_${this.description}_${this.price}`.hashCode();
        }
    }

    // Eksportuj instancję klasy lub funkcję, jeśli jest to potrzebne
    window.Food = Food;
})();