<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inscription</title>

</head>
<body>

	<%@ include file="header.jspf"%>

	<h2>Mon profil</h2>

	<p style="color: red;">${ erreurData }</p>

	<c:forEach var="couple" items="${ mapErreurs }">
		<p style="color: red;">${couple.value}</p>
	</c:forEach>

	<form action="inscription" method="POST">
		<p>
			<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ param.pseudo }" name="pseudo" required />
		</p>

		<p>
			<label for="nom">Nom : </label> <input id="nom" type="text" value="${ param.nom }" name="nom" required />
		</p>

		<p>
			<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ param.prenom }" name="prenom" required />
		</p>

		<p>
			<label for="email">E-mail : </label> <input id="email" type="text" value="${ param.email }" name="email" required />
		</p>

		<p>
			<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ param.telephone }" />
		</p>

		<p>
			<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ param.rue }" required />
		</p>

		<p>
			<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ param.codePostal }" required />
		</p>

		<p>
			<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ param.ville }" required />
		</p>

		<p>
			<label for="mot_de_passe">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse" value="${ param.mot_de_passe }" required />
		</p>

		<p>
			<label for="confirmationMdp">Confirmation : </label> <input id="confirmationMdp" type="password" name="motDePasseBis"
				value="${ param.confirmationMdp }" required />
		</p>

		<p>
			<input type="submit" value="Créer" />  
		</p>

	</form>

	<form action="accueil" method="get">
		<input type="submit" value="Annuler" name="reset" id="reset" />
	</form>

</body>
</html>