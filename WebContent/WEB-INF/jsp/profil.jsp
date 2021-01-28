<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Mes informations personnelles</title>
</head>
<body>

	<%@ include file="header.jspf"%>

	<form action="profil" method="POST">

		<p>
			<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo" disabled />
		</p>

		<p>
			<label for="nom">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom" disabled />
		</p>

		<p>
			<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom" disabled />
		</p>

		<p>
			<label for="email">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email" disabled />
		</p>

		<p>
			<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ profilUtilisateur.telephone }" disabled />
		</p>

		<p>
			<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }" disabled />
		</p>

		<p>
			<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ profilUtilisateur.codePostal }"
				disabled />
		</p>

		<p>
			<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }" disabled />
		</p>

		<p>Crédit : ${ profilUtilisateur.credit }</p>

		<p>
			<input type="submit" value="Modifier" />
		</p>

	</form>
</body>
</html>