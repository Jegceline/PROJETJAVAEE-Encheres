<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vendre un article</title>
</head>
<body>

	<%@ include file="header.jspf"%>

	<h2>Nouvelle vente</h2>
	
		<p style="color: red;">${ erreurData }</p>

	<c:forEach var="couple" items="${ mapErreurs }">
		<p style="color: red;">${couple.value}</p>
	</c:forEach>

	<form action="vente" method="POST">
		<p>
			<label for="libelle">Article : </label> <input id="libelle" type="text" value="${ param.nom_article }" name="nom_article" required />
		</p>

		<p>
			<label for="description">Description : </label>
			<textarea id="description" name="description" rows="4" cols="50" required>${ param.description }</textarea>
		</p>

		<p>
			<%@ include file="listeCategories.jspf"%>
		</p>

		<p>
			<label for="photo">Photo de l'article : </label> <input id="photo" type="file" value="" name="photo" />
		</p>

		<p>
			<label for="prix">Mise à prix : </label><input type="number" id="prix" name="prix_initial" min="0" value="${ param.prix_initial }" required>
		</p>
		<p>
			<label for="debut">Début de l'enchère : </label> <input id="debut" type="date" name="date_debut_encheres" value="${ param.date_debut_encheres }" required />
		</p>
		<p>
			<label for="fin">Fin de l'enchère : </label> <input id="fin" type="date" name="date_fin_encheres" value="${ param.date_fin_encheres }" required/>
		</p>

		<fieldset>

			<legend>Retrait</legend>

			<p>
				<label for="rue">Rue : </label> <input id="rue" type="text" value="${ profilUtilisateur.rue }" name="rue" required />
			</p>

			<p>
				<label for="code_postal">Code postal : </label> <input id="code_postal" type="text" value="${ profilUtilisateur.codePostal }" name="code_postal"
					required />
			</p>

			<p>
				<label for="ville">Ville : </label> <input id="ville" type="text" value="${ profilUtilisateur.ville }" name="ville" required />
			</p>

		</fieldset>

		<p>
			<input type="submit" value="Enregistrer" name="enregistrer" /> <input type="reset" value="Annuler" /> 
		</p>


	</form>


</body>
</html>