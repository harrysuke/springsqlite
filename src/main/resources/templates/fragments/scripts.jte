@param String contextPath
@param boolean withApp
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/axios@1.20.0/dist/axios.min.js" integrity="sha384-mIJWc6qj9ALoBF2yHkNOf0fbGel66+X3UkhYw57p36/PaTyDpO9pvnxrVZ7ZGJfi" crossorigin="anonymous"></script>
<script>
    window.CONTEXT_PATH = "${contextPath}";
</script>
@if (withApp)
<script src="${contextPath}/app.js"></script>
@endif
