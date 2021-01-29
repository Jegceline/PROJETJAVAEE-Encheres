<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vendre un article</title>

<%@ include file="css.jspf"%>
</head>
<body>

	<%@ include file="header.jspf"%>

	<div class="container pt-5">

		<h2>Nouvelle vente</h2>

		<div class="container alert alert-${ alertMessage } ">${ erreurData }</div>

		<p style="color: red;">${ erreurData }</p>

		<c:forEach var="couple" items="${ mapErreurs }">
			<div class="container alert alert-${ alertMessage } ">${couple.value}</div>
		</c:forEach>

		<div class="row">
				
		<form action="vente" method="POST">
			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="libelle" class="form-label">Article : </label> <input id="libelle" type="text" value="${ param.nom_article }" name="nom_article" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="description" class="form-label">Description : </label>
				<textarea id="description" name="description" rows="4" cols="50" class="form-control" required>${ param.description }</textarea>
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<%@ include file="listeCategories.jspf"%>
			</div>
			
			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="photo" class="form-label">Photo de l'article : </label> <input id="photo" type="file" value="" name="photo" class="form-control" />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="prix" class="form-label">Mise à prix : </label><input type="number" id="prix" name="prix_initial" min="0" value="${ param.prix_initial }" class="form-control" required>
			</div>
			
			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="debut" class="form-label">Début de l'enchère : </label> <input id="debut" type="date" name="date_debut_encheres" value="${ param.date_debut_encheres }"
					class="form-control" required />
			</div>
			
			<div class="mb-3 col-sm-12 col-xs-12">
				<label for="fin" class="form-label">Fin de l'enchère : </label> <input id="fin" type="date" name="date_fin_encheres" value="${ param.date_fin_encheres }" class="form-control" required />
			</div>

			<div class="mb-3 col-sm-12 col-xs-12">
			
			<fieldset>

				<legend>Retrait</legend>

				<p>
					<label for="rue" class="form-label">Rue : </label> <input id="rue" type="text" value="${ profilUtilisateur.rue }" name="rue" class="form-control" required />
				</p>

				<p>
					<label for="code_postal" class="form-label">Code postal : </label> <input id="code_postal" type="text" value="${ profilUtilisateur.codePostal }" name="code_postal"
						class="form-control" required />
				</p>

				<p>
					<label for="ville" class="form-label">Ville : </label> <input id="ville" type="text" value="${ profilUtilisateur.ville }" name="ville" class="form-control" required />
				</p>

			</fieldset>
			</div>

			<p>
				<input type="submit" value="Enregistrer" name="enregistrer" /> <input type="reset" value="Annuler" />
			</p>


		</form>
		</div>

	</div>


</body>
</html>