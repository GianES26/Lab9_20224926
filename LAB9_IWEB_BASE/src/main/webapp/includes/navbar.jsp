<nav class="navbar navbar-expand-md navbar-light bg-light">
    <a class="navbar-brand" href="<%=request.getContextPath()%>/PartidoServlet?action=lista">Clasificatorias Sudamericanas Qatar 2022</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/PartidoServlet?action=lista">Partidos</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/ArbitroServlet?action=lista">Arbitros</a>
            </li>
        </ul>
    </div>
</nav>
