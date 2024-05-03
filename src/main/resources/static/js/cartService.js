document.addEventListener("DOMContentLoaded", function() {
    const addToCartButtons = document.querySelectorAll(".add-to-cart");
    const cartElement = document.getElementById("cart");
    const restaurantEmailInput = document.getElementById('restaurantEmailInput');
    const clearCart = sessionStorage.getItem("clearCart");
    
    
    // Odczytanie koszyka z sesji użytkownika
    let cartRequest;
    let foodMap = new Map();
    let totalPrice;
    let shoppingCart;
    
    if(clearCart === "true"){
        sessionStorage.removeItem("cartRequest");
        cartRequest = new Map();
        sessionStorage.setItem("clearCart", "false");
        console.log("clearCart: ", clearCart);
        console.log("Koszyk został wyczyszczony. ", cartRequest);
    }else {
        sessionStorage.setItem("clearCart", "false");
        cartRequest = new Map(JSON.parse(sessionStorage.getItem("cartRequest")));
        
        console.log("Koszyk nie został wyczyszczony.");
        console.log("clearCart: ", clearCart);
    }
    
    updateCartDisplay();
    readingUserSesionAndMapToJSON();
    
    document.querySelectorAll(".add-to-cart").forEach(button => {
        button.addEventListener("click", addToCart);
    });
    
    document.querySelectorAll(".remove-from-cart").forEach(button => {
        button.addEventListener("click", removeFromCart);
    });

    function removeFromCart(event) {
        const foodId = event.target.dataset.foodId;
        const foodCategory = event.target.dataset.foodCategory;
        const foodName = event.target.dataset.foodName;
        const foodDescription = event.target.dataset.foodDescription;
        const foodPrice = event.target.dataset.foodPrice;
        let food = new Food(foodId, foodCategory, foodName, foodDescription, foodPrice);

        // Sprawdzenie, czy dany produkt już istnieje w koszyku
        if(isFoodAlreadyInCart(food)){
            let existingFood = getExistingFood(food);
            let quantity = cartRequest.get(existingFood);

            if(quantity === 1){
                cartRequest.delete(existingFood);
            } else {
                cartRequest.set(existingFood, quantity - 1);
            }
        } 
        // Aktualizacja widoku koszyka
        updateCartDisplay();
        readingUserSesionAndMapToJSON();
    }

    function addToCart(event) {
        const foodId = event.target.dataset.foodId;
        const foodCategory = event.target.dataset.foodCategory;
        const foodName = event.target.dataset.foodName;
        const foodDescription = event.target.dataset.foodDescription;
        const foodPrice = event.target.dataset.foodPrice;
        let food = new Food(foodId, foodCategory, foodName, foodDescription, foodPrice);
        console.log("food: ", food);

        // Sprawdzenie, czy dany produkt już istnieje w koszyku
        if(isFoodAlreadyInCart(food)){
            let existingFood = getExistingFood(food);
            let quantity = cartRequest.get(existingFood);
            cartRequest.set(existingFood, quantity + 1);
            console.log("isAlreadyExist");
        } else{
            cartRequest.set(food, 1);
            console.log("No exist, its first");
        }
        // Aktualizacja widoku koszyka
        updateCartDisplay();
        readingUserSesionAndMapToJSON();
    }

    function updateCartDisplay() {
        const cartElement = document.getElementById("cart");
        cartElement.innerHTML = ""; // Wyczyszczenie zawartości koszyka przed aktualizacją
        
        const tableHeaderTitle = document.createElement("h4");
        tableHeaderTitle.innerHTML = `
            <h4 class="card-title text-center my-2">Your Cart</h4>    
        `;
        cartElement.appendChild(tableHeaderTitle);

        // Utworzenie nagłówków tabeli
        const tableHeader = document.createElement("thead");
        tableHeader.innerHTML = `
            <tr>
                <th>Food Name</th>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
        `;
        const tableBody = document.createElement("tbody");
        totalPrice = 0.0;
        let priceSum = 0.0;

        // Iteracja po elementach koszyka i aktualizacja widoku
        for (const [key, value] of cartRequest) {
            const row = document.createElement("tr");
            const foodNameCell = document.createElement("td");
            foodNameCell.textContent = key.name;
            const quantityCell = document.createElement("td");
            quantityCell.textContent = value;
            const priceCell = document.createElement("td");
            priceCell.textContent = key.price + " zł";
            
            priceSum = key.price * value;
            totalPrice = totalPrice + priceSum;

            row.appendChild(foodNameCell);
            row.appendChild(quantityCell);
            row.appendChild(priceCell);

            tableBody.appendChild(row);
        }

        // Dodanie nagłówków i ciała tabeli do elementu koszyka
        const table = document.createElement("table");
        table.classList.add("table");
        table.appendChild(tableHeader);
        table.appendChild(tableBody);
        cartElement.appendChild(table);

        // Dodanie całkowitej ceny do koszyka
        const totalPriceElement = document.createElement("div");
        totalPriceElement.classList.add("d-flex", "justify-content-between", "fw-bold", "mx-2");
        totalPriceElement.innerHTML = `
            <span class="card-text">Total Price:</span>
            <div class="d-flex px-5">
                <span class="card-text mx-1">${totalPrice}</span>
                <span class="card-text"> zł</span>
            </div>
        `;
        cartElement.appendChild(totalPriceElement);

        // Dodanie przycisku zatwierdzającego zamówienie
        const orderButtonElement = document.createElement("div");
        orderButtonElement.classList.add("row", "justify-content-center", "m-2", "px-3");
        orderButtonElement.innerHTML = `
            <div class="col-md-4">
                <form action="/ordering-food-application/order/cart-summary" method="get" id="checkoutForm">
                    <input type="hidden" name="totalPrice" value="${totalPrice}"></input>
                    <input type="hidden" name="foodKeys" id="foodKeyInput"></input>
                    <input type="hidden" name="foodValues" id="foodValueInput"></input>
                    <input type="hidden" name="restaurantEmail" value="${restaurantEmailInput.value}">
                    <button type="submit" class="btn btn-success go-to-checkout px-4" id="goToCheckout">Checkout</button>
                </form>
            </div>
        `;

        cartElement.appendChild(orderButtonElement);

        // Zapisanie koszyka w sesji użytkownika
        sessionStorage.setItem("cartRequest", JSON.stringify(Array.from(cartRequest)));
    }

    function readingUserSesionAndMapToJSON() {
        // Usunięcie istniejącej mapy przed ponownym zainicjowaniem
        foodMap.clear();

        // Odczytanie koszyka z sesji użytkownika
        cartRequest = new Map(JSON.parse(sessionStorage.getItem("cartRequest")))
        console.log("cartRequest: ", cartRequest);

        cartRequest.forEach((value, key) => {
            foodMap.set(new Food(key.foodId, key.category, key.name, key.description, key.price), value);
        });
        shoppingCart = new Cart(foodMap, totalPrice);
        console.log("shoppingCart: ", shoppingCart);
        console.log("foodMap: ", foodMap);
        console.log("/n");

        //  Przekonwertowanie Mapy na JSONa
        const foodKeyJSON = JSON.stringify(Array.from(foodMap.keys()));
        const foodValueJSON = JSON.stringify(Array.from(foodMap.values()));
        document.getElementById('foodKeyInput').value = foodKeyJSON;
        document.getElementById('foodValueInput').value = foodValueJSON;
    }

    function isFoodAlreadyInCart(food) {
        for (const [existingFood, _] of cartRequest) {
            if(existingFood.foodId === food.foodId){
                return true;
            }
        }
        return false;
    }

    function getExistingFood(food) {
        for (const existingFood of cartRequest.keys()) {
            if (existingFood.foodId === food.foodId) {
                return existingFood;
            }
        }
        return null;
    }
});