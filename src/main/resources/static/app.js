(function() {
  var form = document.getElementById("userForm");
  var message = document.getElementById("formMessage");
  var modalEl = document.getElementById("addUserModal");

  function show(kind, text) {
    if (!message) return;
    message.className = "app-alert app-alert-" + kind;
    message.textContent = text;
    message.style.display = "block";
  }

  function hideMessage() {
    if (message) {
      message.style.display = "none";
      message.textContent = "";
    }
  }

  if (form) {
    form.addEventListener("submit", async function(event) {
      event.preventDefault();
      hideMessage();
      var payload = Object.fromEntries(new FormData(form));
      show("info", "Creating user...");

      try {
        var baseUrl = window.CONTEXT_PATH || "";
        var res = await axios.post(baseUrl + "/api/users", payload);
        var data = res.data;
        if (typeof data !== "object" || data === null) {
          show("danger", "Session expired - please sign in again.");
          return;
        }
        show("success", 'User "' + data.name + '" created (ID ' + data.id + ").");
        form.reset();

        // Close modal after short delay so user sees success message
        setTimeout(function() {
          if (modalEl) {
            var modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) modal.hide();
          }
          // Reload page to show the new user in the table
          window.location.reload();
        }, 1200);

      } catch (err) {
        var detail = err.response && err.response.data ? err.response.data : {};
        if (detail.fields) {
          show("danger", Object.values(detail.fields).join("; "));
        } else if (detail.error) {
          show("danger", detail.error);
        } else if (err.response) {
          show("danger", "Request failed (HTTP " + err.response.status + ")");
        } else {
          show("danger", "Network error");
        }
      }
    });
  }

  // Clear form and messages when modal is dismissed
  if (modalEl) {
    modalEl.addEventListener("hidden.bs.modal", function() {
      if (form) form.reset();
      hideMessage();
    });
    modalEl.addEventListener("show.bs.modal", function() {
      hideMessage();
    });
  }
})();
