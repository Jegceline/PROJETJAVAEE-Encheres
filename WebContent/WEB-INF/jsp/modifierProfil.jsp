<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier mes informations</title>

<%@ include file="css.jspf"%>
</head>
<body>

	<%@ include file="header.jspf"%>

	${ succesModif }

	<c:forEach var="couple" items="${ mapErreurs }">
		<p style="color: red;">${couple.value}</p>
	</c:forEach>

	<div class="container pt-5">

		<form action="modifier-profil" method="POST">

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo" class="form-control"
					required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="nom">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom" class="form-control"
					required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="email">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ profilUtilisateur.telephone }"
					class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ profilUtilisateur.codePostal }"
					class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }" class="form-control"
					required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="mot_de_passe">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse"
					value="${ profilUtilisateur.motDePasse}" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="mot_de_passe">Confirmation : </label> <input id="mot_de_passe" type="password" name="motDePasseBis"
					value="${ profilUtilisateur.motDePasse}" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<p>Crédit : ${ profilUtilisateur.credit }</p>
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<button class="btn btn-success " name="enregistrer">Modifier</button>
				<button class="btn btn-success " name="supprimer">Supprimer mon compte</button>
			</div>

		</form>

	</div>

</body>
</html>