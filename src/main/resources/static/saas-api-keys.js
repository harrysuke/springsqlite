(function () {
  var keyForm = document.getElementById("apiKeyForm");
  var keyMessage = document.getElementById("formMessage");

  function getCsrfHeaders() {
    var h = {};
    var cookie = document.cookie.split('; ').find(function (c) { return c.startsWith('XSRF-TOKEN='); });
    if (cookie) h['X-XSRF-TOKEN'] = decodeURIComponent(cookie.split('=')[1]);
    return h;
  }

  function showMsg(kind, text) {
    if (!keyMessage) return;
    keyMessage.className = 'app-alert app-alert-' + kind;
    keyMessage.style.display = 'block';
    keyMessage.textContent = text;
  }

  if (keyForm) {
    keyForm.addEventListener("submit", async function (e) {
      e.preventDefault();
      var name = document.getElementById("keyName").value;
      showMsg("secondary", "Generating API key...");
      try {
        var resp = await axios.post(window.API_KEYS_URL, { name: name }, { headers: getCsrfHeaders() });
        var data = resp.data;
        if (typeof data !== 'object' || data === null) {
          showMsg("danger", "Session expired - please sign in again.");
          return;
        }
        showMsg("success", 'Key "' + data.name + '" created: ' + data.keyValue);
        setTimeout(function () { location.reload(); }, 1500);
      } catch (err) {
        var detail = err.response ? err.response.data : {};
        showMsg("danger", detail.error || "Failed to create key");
      }
    });
  }

  window.toggleKey = async function (id) {
    try {
      await axios.put(window.API_KEYS_TOGGLE_URL(id), {}, { headers: getCsrfHeaders() });
      location.reload();
    } catch (err) {
      alert("Failed to toggle key: " + (err.response && err.response.data ? err.response.data.error : err.message));
    }
  };

  window.deleteKey = async function (id) {
    if (!confirm("Delete this API key?")) return;
    try {
      await axios.delete(window.API_KEYS_DELETE_URL(id), { headers: getCsrfHeaders() });
      location.reload();
    } catch (err) {
      alert("Failed to delete key: " + (err.response && err.response.data ? err.response.data.error : err.message));
    }
  };
})();
