<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Connexion</title>

<%@ include file="css.jspf"%>
</head>

<body class="body-flex">

	<%@ include file="header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">



			<h2 class="my-4 text-center">Se connecter</h2>


			<div class="card card-signin flex-row m-auto col-sm-8 col-xs-12">
				<div class="card-body">
						<form action="connexion" method="POST">

					<div class="mb-3 col-sm-12 col-xs-12">
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
							<input id="memoriserUtilisateur" type="checkbox" name="memoriserUtilisateur" /> <label for="memoriserUtilisateur" class="form-label">Se
								souvenir de moi</label>
						</p>
						
						<p>
						<a href="">Mot de passe oublié</a>
					</p>
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<button class="btn btn-primary ">Connexion</button>
					</div>

					</form>



					<form action="inscription" method="get">

						<div class="mb-3 col-sm-12 col-xs-12">
							<button class="btn btn-primary">Créer un compte</button>
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

	<%@ include file="footer.jspf"%>

</body>
</html>