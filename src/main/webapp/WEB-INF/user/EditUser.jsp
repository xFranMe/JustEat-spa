<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro</title>
	  	<link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
	  	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
	  	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">	
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/editUser.css">
    </head>
    <body>
        <!--Header-->
        <header class="content"> 
            <img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo.png" alt="Just Eat Logo">
            <a id="text-header" href="LoginServlet.do">Iniciar sesión</a>
            <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">          
        </header>

        <!--Sign Up-->
        <div class="register-body content">
            <div class="register-body-box">
                <div class="register-body-text">
                    <h1>Crear cuenta</h1>
                    <a  href="LoginServlet.do">¿Ya formas parte de Just Eat?</a>
        		</div>
              
              	<c:forEach items="${messages}" var="error">
        			<p class="error-text">${error.value}</p>
    			</c:forEach>       
              	
              	<!--Este JSP puede ser invocado desde dos servlet (registro de un nuevo usuario y edición de usuario existente)-->
                <form action="?" method="POST">
                    <label for="name">Nombre</label>
                    <input class="register-form-field" type="text" id="name" name="name" placeholder="Introduce tu nombre" value="${requestScope.user.name}" required>
            
                    <label for="surname">Apellido</label>
                    <input class="register-form-field" type="text" id="surname" name="surname" placeholder="Introduce tus apellidos" value="${requestScope.user.surname}" required>
            
                    <label for="email">Correo electrónico</label>
                    <input class="register-form-field" type="email" id="email" name="email" placeholder="Introduce tu email" value="${requestScope.user.email}" required>
                    
                    <label for="password">Contraseña</label>
                    <input class="register-form-field" type="password" id="password" name="password" placeholder="Introduce tu contraseña" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$" value="${requestScope.user.password}" required>
                    <p class="notes">La contraseña debe contener 8 dígitos entre los que se debe incluir como mínimo un número, una mayúscula, una minúscula y un carcácter especial.</p>
                        
                    <input id="register-form-submit" type="submit" value="Crear cuenta">
                </form>

            </div>
        </div>
    </body>
</html>