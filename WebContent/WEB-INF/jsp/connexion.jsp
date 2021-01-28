<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Page d'accueil</title>
</head>

<body>
	<%@ include file="header.jspf"%>

	<p style="color: red;">${ erreurMotDePasse }</p>

	<c:forEach var="couple" items="${ mapErreurs }">
		<p style="color: red;">${couple.value}</p>
	</c:forEach>


	<form action="connexion" method="POST">
		<p>
			<label for="identifiant">Identifiant : </label> <input id="identifiant" type="text" value="" name="identifiant" required />
		</p>

		<p>
			<label for="motdepasse">Mot de passe : </label> <input id="motdepasse" type="text" value="" name="motdepasse" required />
		</p>

		<p>
			<label for="memoriserUtilisateur">Se souvenir de moi</label> <input id="memoriserUtilisateur" type="checkbox" name="memoriserUtilisateur" />
		</p>

		<p>
			<input type="submit" value="Connexion" />
		</p>

	</form>

	<p>
		<a href="">Mot de passe oublié</a>
	</p>

	<form action="inscription" method="get">
		<input type="submit" value="Créer un compte" name="Submit" id="creationCompte" />
	</form>


</body>
</html>