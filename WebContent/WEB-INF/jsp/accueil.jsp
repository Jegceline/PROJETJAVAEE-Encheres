<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Accueil</title>
</head>
<body>

	<%@ include file="header.jspf"%>

	<c:if test="${empty sessionScope.profilUtilisateur }">

		<a href="inscription">S'inscrire</a> -
	<a href="connexion">Se connecter</a>

	</c:if>

	<c:if test="${not empty sessionScope.profilUtilisateur }">

		<a href="">Enchères</a> -
	<a href="vente">Vendre un article</a> -
	<a href="profil">Mon Profil</a> -
	<a href="deconnexion">Déconnexion</a>

	</c:if>

	<h2>Liste des enchères</h2>
	
			${mapErreurs}

	<form action="" method="POST">

		<h3>Filtres</h3>

		<%@ include file="listeCategories.jspf"%>

		<div>
			<p>
				<input type="radio" id="achats" name="achats" value="achats" disabled> <label for="achats">Achats</label>
			</p>
			<p>
				<input type="checkbox" id="achats1" name="achats" value="encheres_ouvertes" checked> <label for="achats1">enchères ouvertes</label>
			</p>
			<p>
				<input type="checkbox" id="achats2" name="achats" value="mes_encheres" disabled> <label for="achats2">mes enchères</label>
			</p>
			<p>
				<input type="checkbox" id="achats3" name="achats" value="mes_encheres_remportees" disabled> <label for="achats3">mes enchères remportées</label>
			</p>
		</div>

		<div>
			<p>
				<input type="radio" id="ventes" name="ventes" value="ventes" disabled> <label for="ventes">Mes ventes</label>
			</p>
			<p>
				<input type="checkbox" id="ventes1" name="achats" value="encheres_ouvertes" disabled> <label for="ventes1">enchères ouvertes</label>
			</p>
			<p>
				<input type="checkbox" id="ventes2" name="achats" value="mes_encheres" disabled> <label for="ventes2">mes enchères</label>
			</p>
			<p>
				<input type="checkbox" id="ventes3" name="achats" value="mes_encheres_remportees" disabled> <label for="ventes3">mes enchères remportées</label>
			</p>
		</div>

		<input type="submit" value="Rechercher" name="rechercher" />

	</form>
		
		
		<h3> Enchères ouvertes </h3>
		
		<p>${encheresEC}</p>
		
	<c:forEach var="article" items="${encheresEC}">
			<p> ${article.nomArticle} </p>
		</c:forEach>

</body>
</html>