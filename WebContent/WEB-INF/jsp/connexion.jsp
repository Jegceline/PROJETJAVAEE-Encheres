<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Page d'accueil</title>

<%@ include file="css.jspf"%>
</head>

<body>
	<%@ include file="header.jspf"%>
	

	<div class="container pt-5">
	
		<div class="container alert alert-${ alertMessage } ">${ erreurMotDePasse }
		</div>
	

		<p style="color: red;">${ erreurMotDePasse }</p>

		<c:forEach var="couple" items="${ mapErreurs }">
			<p style="color: red;">${couple.value}</p>
		</c:forEach>

		<div class="card card-signin flex-row m-auto col-sm-8 col-xs-12">
			<div class="card-body">
			
				<div class="mb-3 col-sm-12 col-xs-12">
					<form action="connexion" method="POST">
						<p>
							<label for="identifiant" class="form-label">Identifiant : </label> <input id="identifiant" type="text" value="" name="identifiant"
								class="form-control" required />
						</p>
				</div>

				<div class="mb-3 col-sm-12 col-xs-12">
					<p>
						<label for="motdepasse" class="form-label">Mot de passe : </label> <input id="motdepasse" type="text" value="" name="motdepasse"
							class="form-control" required />
					</p>
				</div>

				<div class="mb-3 col-sm-12 col-xs-12">
					<p>
						<input id="memoriserUtilisateur" type="checkbox"
							name="memoriserUtilisateur" />
							<label for="memoriserUtilisateur" class="form-label">Se souvenir de moi</label> 
					</p>
				</div>

				<div class="mb-3 col-sm-12 col-xs-12">
					<button class="btn btn-success ">Connexion</button>
				</div>

				</form>

				<p>
					<a href="">Mot de passe oublié</a>
				</p>

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


</body>
</html>