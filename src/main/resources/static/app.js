const CONTEXT_PATH = window.CONTEXT_PATH || "";
const CSRF_TOKEN = window.CSRF_TOKEN || "";
const form = document.getElementById("userForm");
const formMessage = document.getElementById("formMessage");

function showMessage(kind, text) {
  if (!formMessage) return;
  formMessage.className = `alert alert-${kind}`;
  formMessage.textContent = text;
}

if (form) {
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    showMessage("secondary", "Creating user...");
    try {
      const res = await fetch(`${CONTEXT_PATH}/api/users`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-XSRF-TOKEN": CSRF_TOKEN,
        },
        body: JSON.stringify({ name, email, password }),
      });
      const data = await res.json().catch(() => ({}));
      if (res.ok) {
        showMessage("success", `User "${data.name}" created (ID ${data.id}).`);
        form.reset();
      } else {
        const fields = data.fields;
        const msg = fields ? Object.values(fields).join("; ") : data.error || "Failed to create user";
        showMessage("danger", msg);
      }
    } catch (err) {
      showMessage("danger", "Network error: " + err.message);
    }
  });
}