const apiUrl = "http://localhost:8081/api/cars";

// LOAD CARS
function loadCars() {

    fetch(apiUrl)
    .then(response => response.json())
    .then(cars => {

        const container = document.getElementById("cars-container");
        container.innerHTML = "";

        cars.forEach(car => {

            const carCard = document.createElement("div");
            carCard.className = "car-card";

            carCard.innerHTML = `
                <h3>${car.name}</h3>
                <p>Brand: ${car.brand}</p>
                <p>Price: $${car.price}</p>
                <img src="images/${car.image}" width="200"/>
                <br><br>
                <button onclick='addToCart(${JSON.stringify(car)})'>
                    Add to Cart
                </button>
            `;

            container.appendChild(carCard);
        });

    })
    .catch(error => console.error("Error loading cars:", error));
}


// ✅ ADD TO CART FUNCTION (NEW)
function addToCart(car) {

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    cart.push(car);

    localStorage.setItem("cart", JSON.stringify(cart));

    alert(car.name + " added to cart!");
}


// LOAD ONLY ON CARS PAGE
if (window.location.pathname.includes("cars.html")) {
    loadCars();
}

