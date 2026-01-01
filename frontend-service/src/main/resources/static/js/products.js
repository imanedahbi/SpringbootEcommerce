const apiUrl = "http://localhost:8083/products";
let PRODUCTS = [];


/* ---------- Charger produits ---------- */
async function loadProducts() {
    try {
        const res = await fetch(apiUrl);
        if (!res.ok) throw new Error(await res.text());
        PRODUCTS = await res.json();
        renderProducts();
    } catch (err) {
        console.error("Erreur chargement produits :", err);
        document.getElementById("product-list").innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger mb-0">
                    Impossible de charger les produits. <br><small>${err.message}</small>
                </div>
            </div>`;
    }
}

/* ---------- Afficher produits ---------- */
function renderProducts() {
    const container = document.getElementById("product-list");
    container.innerHTML = "";
    const q = (document.getElementById("searchInput").value || "").toLowerCase();
    const filtered = PRODUCTS.filter(p => p.name?.toLowerCase().includes(q));

    if (filtered.length === 0) {
        container.innerHTML = `<div class="col-12"><div class="alert alert-info mb-0">Aucun produit trouvé</div></div>`;
        return;
    }

    filtered.forEach(p => {
        const imgSrc = p.image ? `http://localhost:8083${p.image}` : "https://via.placeholder.com/300x200?text=Pas+d'image";
        const col = document.createElement("div");
        col.className = "col-sm-6 col-md-4";

        // Lire le rôle **ici**, dynamiquement
        const role = localStorage.getItem("role");
        let adminButtons = "";
        if (role === "ROLE_ADMIN") {
            adminButtons = `
                <button class="btn btn-sm btn-outline-primary" onclick="editProduct(${p.id})">Modifier</button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${p.id})">Supprimer</button>
            `;
        }

        col.innerHTML = `
<div class="card h-100 shadow-sm">
    <img src="${imgSrc}" class="card-img-top" alt="${p.name}">
    <div class="card-body d-flex flex-column">
        <h5 class="card-title mb-1">${p.name}</h5>
        <p class="card-text small mb-1 text-truncate">${p.description || ""}</p>
        <p class="mb-2"><strong>${Number(p.price).toFixed(2)} Dh</strong> • <span class="badge bg-secondary">${p.category}</span></p>
        <div class="mt-auto d-grid gap-2">
            ${adminButtons}
            <button class="btn btn-sm btn-outline-success" onclick="addToCart(${p.id})">Ajouter au panier</button>
        </div>
    </div>
</div>`;

        container.appendChild(col);
    });
}

/* ---------- Upload image ---------- */
async function uploadFileInput(fileInput) {
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) return "";
    const fd = new FormData();
    fd.append("image", fileInput.files[0]);
    try {
        const res = await fetch(apiUrl + "/upload", { method: "POST", body: fd });
        if (!res.ok) throw new Error(await res.text());
        const data = await res.json();
        return data.imageUrl || "";
    } catch (err) {
        console.error("Erreur upload image:", err);
        alert("Erreur upload image : " + err.message);
        return "";
    }
}

/* ---------- Ajouter produit ---------- */
document.getElementById("addBtn").addEventListener("click", async () => {
    const name = document.getElementById("name").value.trim();
    const category = document.getElementById("category").value;
    const price = parseFloat(document.getElementById("price").value || 0);
    const description = document.getElementById("description").value.trim();

    if (!name || !category || isNaN(price)) {
        alert("Remplis les champs obligatoires (nom, catégorie, prix).");
        return;
    }

    try {
        const imageUrl = await uploadFileInput(document.getElementById("imageFile"));
        const product = { name, category, price, description, image: imageUrl };

        const res = await fetch(apiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(product)
        });
        if (!res.ok) throw new Error(await res.text());

        document.getElementById("name").value = "";
        document.getElementById("category").value = "";
        document.getElementById("price").value = "";
        document.getElementById("description").value = "";
        document.getElementById("imageFile").value = "";

        await loadProducts();
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
});

/* ---------- Supprimer produit ---------- */
async function deleteProduct(id) {
    if (!confirm("Supprimer ce produit ?")) return;
    try {
        const res = await fetch(`${apiUrl}/${id}`, { method: "DELETE" });
        if (!res.ok) throw new Error(await res.text());
        await loadProducts();
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

/* ---------- Modifier produit ---------- */
function editProduct(id) {
    const product = PRODUCTS.find(p => p.id === id);
    if (!product) return;

    document.getElementById("editProductId").value = product.id;
    document.getElementById("editName").value = product.name;
    document.getElementById("editCategory").value = product.category;
    document.getElementById("editPrice").value = product.price;
    document.getElementById("editDescription").value = product.description;
    document.getElementById("editImageFile").value = "";

    const modal = new bootstrap.Modal(document.getElementById('editProductModal'));
    modal.show();
}

document.getElementById("saveEditBtn").addEventListener("click", async () => {
    const id = document.getElementById("editProductId").value;
    const product = PRODUCTS.find(p => p.id == id);
    if (!product) return;

    const name = document.getElementById("editName").value.trim() || product.name;
    const category = document.getElementById("editCategory").value || product.category;
    const priceInput = document.getElementById("editPrice").value;
    const price = priceInput === "" ? product.price : parseFloat(priceInput);
    const description = document.getElementById("editDescription").value.trim() || product.description;

    try {
        let image = product.image;
        const fileInput = document.getElementById("editImageFile");
        if (fileInput.files.length > 0) {
            image = await uploadFileInput(fileInput);
        }

        const updatedProduct = { name, category, price, description, image };

        const res = await fetch(`${apiUrl}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedProduct)
        });
        if (!res.ok) throw new Error(await res.text());

        const modalEl = document.getElementById('editProductModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        modal.hide();

        await loadProducts();
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
});

/* ---------- Ajouter au panier ---------- */
async function addToCart(productId) {
    // ==== AJOUTEZ CES 2 LIGNES ====
    console.log("🟢🟢🟢 addToCart CLICKÉ ! ID:", productId);
    alert("Bouton cliqué! Produit ID: " + productId);
    // ==============================

    const userId = Number(localStorage.getItem("userId"));

    console.log("🔄 Ajout au panier - userId:", userId, "productId:", productId);

    if (!userId || Number.isNaN(userId)) {
        alert("Vous devez être connecté pour ajouter un produit au panier.");
        return;
    }

    const quantity = 1;

    try {
        console.log("📤 Envoi requête à /cart/add:", { userId, productId, quantity });

        const res = await fetch("http://localhost:8080/cart/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify({ userId, productId, quantity })
        });

        console.log("📥 Réponse reçue:", res.status, res.statusText);

        if (!res.ok) {
            const errorText = await res.text();
            console.error("❌ Erreur serveur:", errorText);
            throw new Error(errorText);
        }

        const result = await res.json();
        console.log("✅ Produit ajouté avec succès:", result);

        alert("✅ Produit ajouté au panier !");

    } catch (err) {
        console.error("❌ Erreur ajout au panier :", err);
        alert("Erreur ajout au panier : " + err.message);
    }
}
/* ---------- Initialisation ---------- */
/* ---------- Initialisation ---------- */
document.addEventListener("DOMContentLoaded", () => {
    const role = localStorage.getItem("role") || "";
    console.log("👑 ROLE AU CHARGEMENT PRODUCTS:", role);

    // Formulaire admin
    const addForm = document.getElementById("adminProductForm");
    if (addForm) {
        // Masquer par défaut
        addForm.style.display = "none";

        // Afficher seulement si role contient "ADMIN"
        if (role.toLowerCase().includes("admin")) {
            addForm.style.display = "block";
        }
    }

    // Charger produits et ajouter événements
    loadProducts();
    document.getElementById("refreshBtn").addEventListener("click", loadProducts);
    document.getElementById("searchInput").addEventListener("input", renderProducts);
});
