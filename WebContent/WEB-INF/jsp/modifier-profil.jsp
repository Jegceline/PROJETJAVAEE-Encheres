<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier mes informations</title>

<%@ include file="css.jspf"%>
</head>
<body class="body-flex">

	<%@ include file="header.jspf"%>
	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="my-4">Modifier mon profil</h2>
			<hr>

			<c:if test="${ not empty succesModif}">
				<div class="alert alert-success" role="alert">${succesModif}</div>
			</c:if>


			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>




			<form action="modifier-profil" method="POST">

				<div class="row my-2">
					<div class="col">
						<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo" class="form-control"
							required />
					</div>

					<div class="col">
						<label for="nom">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom" class="form-control" required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">

						<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom" class="form-control"
							required />
					</div>

					<div class="col">
						<label for="email">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email" class="form-control"
							required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ profilUtilisateur.telephone }"
							class="form-control" />
					</div>

					<div class="col">
						<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }" class="form-control" required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ profilUtilisateur.codePostal }"
							class="form-control" required />
					</div>

					<div class="col">
						<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }" class="form-control"
							required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="mot_de_passe">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse"
							value="${ profilUtilisateur.motDePasse}" class="form-control" required />
					</div>

					<div class="col">
						<label for="mot_de_passe">Confirmation : </label> <input id="mot_de_passe" type="password" name="motDePasseBis"
							value="${ profilUtilisateur.motDePasse}" class="form-control" required />
					</div>
				</div>

				<div class="row my-4">
					<div class="col">
						<button class="btn btn-info" name="enregistrer">Enregistrer les modifications</button>
						<button class="btn ml-4 btn-danger" name="supprimer">Supprimer mon compte</button>
					</div>
				</div>
			</form>
			
							<form action="profil" method="GET">
					<input type="submit" value="Annuler" name="reset" id="reset" class="btn btn-secondary"/>
				</form>

		</div>
	</div>

	<%@ include file="footer.jspf"%>

</body>
</html>