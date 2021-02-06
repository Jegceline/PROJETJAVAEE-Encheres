<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Connexion</title>

<%@ include file="jspf/css.jspf"%>
</head>

<body class="body-flex">

	<%@ include file="jspf/header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">



			<h2 class="my-4 text-center">Se connecter</h2>
			
			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>

			
			<c:if test="${ not empty utilisateurInconnu}">
				<div class="alert alert-danger" role="alert">${utilisateurInconnu}</div>
			</c:if>
			
			<c:if test="${ not empty erreurMotDePasse}">
				<div class="alert alert-danger" role="alert">${erreurMotDePasse}</div>
			</c:if>


			<div class="card card-signin flex-row m-auto col-sm-8 col-xs-12">
				<div class="card-body">
						<form action="<%=request.getContextPath()%>/connexion" method="POST">

					<div class="mb-3 col-sm-12 col-xs-12">
							<p>
								<label for="identifiant" class="form-label">Identifiant : </label> <input id="identifiant" type="text" value="${cookie.cookieUserId.value}" name="identifiant"
									class="form-control" required />
							</p>
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<p>
							<label for="motdepasse" class="form-label">Mot de passe : </label> <input id="motdepasse" type="password" value="${cookie.cookieUserPassword.value}" name="motdepasse"
								class="form-control" required />
						</p>
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<p>
							<input id="memoriserUtilisateur" type="checkbox" name="memoriserUtilisateur"/> <label for="memoriserUtilisateur" class="form-label">Se
								souvenir de moi</label>
						</p>
						
						<p>
						<a href="" class="disabled">Mot de passe oublié</a>
					</p>
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<button class="btn btn-success ">Connexion</button>
					</div>

					</form>



					<form action="inscription" method="get">

						<div class="mb-3 col-sm-12 col-xs-12">
							<button class="btn btn-success">Créer un compte</button>
						</div>

					</form>

					<!--  
				<form action="inscription" method="get">
					<input type="submit" value="Créer un compte" name="Submit" id="creationCompte" />
				</form> 
				
				-->

				</div>
			</div>
		</div>
	</div>

	<%@ include file="jspf/footer.jspf"%>

</body>
</html>