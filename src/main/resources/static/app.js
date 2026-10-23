const form = document.getElementById("userForm");
const message = document.getElementById("formMessage");

function show(kind, text) {
  if (!message) return;
  message.className = `alert alert-${kind}`;
  message.textContent = text;
}

form?.addEventListener("submit", async (event) => {
  event.preventDefault();
  const payload = Object.fromEntries(new FormData(form));
  show("secondary", "Creating user...");
  try {
    // Axios reads the non-HttpOnly XSRF-TOKEN cookie and sends the
    // X-XSRF-TOKEN header by itself; it also sets Content-Type: application/json.
    const { data } = await axios.post("api/users", payload);
    if (typeof data !== "object" || data === null) {
      show("danger", "Session expired - please sign in again.");
      return;
    }
    show("success", `User "${data.name}" created (ID ${data.id}).`);
    form.reset();
  } catch (err) {
    const detail = err.response?.data ?? {};
    show("danger", detail.fields
      ? Object.values(detail.fields).join("; ")
      : detail.error ?? (err.response ? `Request failed (HTTP ${err.response.status})` : "Network error"));
  }
});
