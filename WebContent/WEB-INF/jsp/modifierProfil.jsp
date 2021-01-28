<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier mes informations</title>
</head>
<body>

	<%@ include file="header.jspf"%>

	${ succesModif }

	<c:forEach var="couple" items="${ mapErreurs }">
		<p style="color: red;">${couple.value}</p>
	</c:forEach>

	<form action="modifier-profil" method="POST">

		<p>
			<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo" required />
		</p>

		<p>
			<label for="nom">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom" required />
		</p>

		<p>
			<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom" required />
		</p>

		<p>
			<label for="email">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email" required />
		</p>

		<p>
			<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ profilUtilisateur.telephone }" />
		</p>

		<p>
			<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }" required />
		</p>

		<p>
			<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ profilUtilisateur.codePostal }"
				required />
		</p>

		<p>
			<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }" required />
		</p>

		<p>
			<label for="mot_de_passe">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse"
				value="${ profilUtilisateur.motDePasse}" required />
		</p>

		<p>
			<label for="mot_de_passe">Confirmation : </label> <input id="mot_de_passe" type="password" name="motDePasseBis"
				value="${ profilUtilisateur.motDePasse}" required />
		</p>

		<p>Crédit : ${ profilUtilisateur.credit }</p>

		<p>
			<input type="submit" value="Enregistrer" name="enregistrer" /> <input type="submit" value="Supprimer mon compte" name="suppression" />
		</p>

	</form>

</body>
</html>