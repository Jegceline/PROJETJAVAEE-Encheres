<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inscription</title>

<%@ include file="css.jspf"%>

</head>
<body>

	<div class="container">
		<%@ include file="header.jspf"%>

		<div class="container alert alert-${ alertMessage } ">
			<p style="color: red;">${ erreurData }</p>

			<c:forEach var="couple" items="${ mapErreurs }">
				<p style="color: red;">${couple.value}</p>
			</c:forEach>

		</div>

		<section class="container pt-5">

			<h2>Mon profil</h2>

			<div class="card-body">

				<form action="inscription" method="POST">

					<div class="row">
						<div class="col">
							<label for="pseudo" class="form-label">Pseudo : </label> <input id="pseudo" type="text" value="${ param.pseudo }" name="pseudo"
								class="form-control" placeholder="Seuls les caractères alphanumériques sont autorisés" required />
						</div>

						<div class="col">
							<label for="nom" class="form-label">Nom : </label> <input id="nom" type="text" value="${ param.nom }" name="nom" class="form-control" required />
						</div>
					</div>

					<div class="row my-2">
						<div class="col">
							<label for="prenom" class="form-label">Prénom : </label> <input id="prenom" type="text" value="${ param.prenom }" name="prenom"
								class="form-control" required />
						</div>

						<div class="col">
							<label for="email" class="form-label">E-mail : </label> <input id="email" type="text" value="${ param.email }" name="email"
								class="form-control" required />
						</div>
					</div>

					<div class="row my-2">
						<div class="col">
							<label for="telephone" class="form-label">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ param.telephone }"
								class="form-control" required />
						</div>

						<div class="col">
							<label for="rue" class="form-label">Rue : </label> <input id="rue" type="text" name="rue" value="${ param.rue }" class="form-control" required />
						</div>
					</div>

					<div class="row my-2">
						<div class="col">
							<label for="code_postal" class="form-label">Code postal :</label> <input id="code_postal" type="text" name="codePostal"
								value="${ param.codePostal }" class="form-control" required />
						</div>

						<div class="col">
							<label for="telephone" class="form-label">Ville : </label> <input id="ville" type="text" name="ville" value="${ param.ville }"
								class="form-control" required />
						</div>
					</div>

					<div class="row my-2">
						<div class="col">
							<label for="mot_de_passe" class="form-label">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse"
								value="${ param.mot_de_passe }" class="form-control" required />
						</div>

						<div class="col">
							<label for="confirmationMdp" class="form-label">Confirmation : </label> <input id="confirmationMdp" type="password" name="motDePasseBis"
								value="${ param.confirmationMdp }" class="form-control" required />
						</div>
					</div>

					<div class="row my-4">
						<div class="col">
							<button class="btn btn-success ">Créer un compte</button>

							<form action="accueil" method="get">
								<input type="submit" value="Annuler" name="reset" id="reset" />
							</form>
						</div>
					</div>

					<!-- <div class="mb-3 col-sm-12 col-xs-12">
					<input type="submit" value="Créer" />
				</div>
				  -->

				</form>


			</div>
</body>
</html>