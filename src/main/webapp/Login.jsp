<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
            <img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo.png" alt="Just Eat Logo">
            <a id="text-header" href="SignUpServlet.do">Regístrate</a>
            <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
        </header>
        
        <!--Login-->
        <div class="login-background content">
            <div class="login-body">
                <h1>Iniciar sesión</h1>
                <p class="error-text">${requestScope.messages.error}</p>
                <form action="LoginServlet.do" method="POST">
                    <label for="email">Email</label>
                    <input class="login-form-field" type="email" id="email" name="email" placeholder="Introduce tu email" value="${requestScope.email}" required>       
                        
                    <label for="password">Contraseña</label>
                    <input class="login-form-field" type="password" id="password" name="password" placeholder="Introduce tu contraseña" value="${requestScope.password}" required>       
                                            
                    <input id="login-form-submit" type="submit" value="Iniciar sesión">         
                </form>
                <p class="text-small">¿Nuevo en Just Eat?<span><a href="SignUpServlet.do">Crear cuenta</a></span></p>     
            </div>
        </div>
      
    </body>
</html>