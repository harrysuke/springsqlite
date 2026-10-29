(function () {
  var whForm = document.getElementById("webhookForm");
  var whMessage = document.getElementById("formMessage");

  function getCsrfHeaders() {
    var h = {};
    var cookie = document.cookie.split('; ').find(function (c) { return c.startsWith('XSRF-TOKEN='); });
    if (cookie) h['X-XSRF-TOKEN'] = decodeURIComponent(cookie.split('=')[1]);
    return h;
  }

  function showMsg(kind, text) {
    if (!whMessage) return;
    whMessage.className = 'app-alert app-alert-' + kind;
    whMessage.style.display = 'block';
    whMessage.textContent = text;
  }

  if (whForm) {
    whForm.addEventListener("submit", async function (e) {
      e.preventDefault();
      var payload = {
        url: document.getElementById("whUrl").value,
        description: document.getElementById("whDesc").value,
        eventTypes: document.getElementById("whEventTypes").value
      };
      showMsg("secondary", "Creating webhook...");
      try {
        await axios.post(window.WEBHOOKS_URL, payload, { headers: getCsrfHeaders() });
        showMsg("success", "Webhook created successfully.");
        setTimeout(function () { location.reload(); }, 1000);
      } catch (err) {
        var detail = err.response ? err.response.data : {};
        showMsg("danger", detail.error || "Failed to create webhook");
      }
    });
  }

  window.toggleWh = async function (id) {
    try {
      await axios.put(window.WEBHOOKS_TOGGLE_URL(id), {}, { headers: getCsrfHeaders() });
      location.reload();
    } catch (err) {
      alert("Failed: " + (err.response && err.response.data ? err.response.data.error : err.message));
    }
  };

  window.deleteWh = async function (id) {
    if (!confirm("Delete this webhook?")) return;
    try {
      await axios.delete(window.WEBHOOKS_DELETE_URL(id), { headers: getCsrfHeaders() });
      location.reload();
    } catch (err) {
      alert("Failed: " + (err.response && err.response.data ? err.response.data.error : err.message));
    }
  };
})();
