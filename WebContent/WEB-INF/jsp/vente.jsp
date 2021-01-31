<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vendre un article</title>

<%@ include file="css.jspf"%>
</head>

<body class="body-flex">

	<%@ include file="header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="my-4">Vendre un article</h2>
							<hr>

			<c:if test="${ not empty erreurData}">
				<div class="alert alert-danger" role="alert">${erreurData}</div>
			</c:if>


			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>



			<div class="row">

				<form action="vente" method="POST">
					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="libelle" class="form-label">Article : </label> <input id="libelle" type="text" value="${ param.nom_article }" name="nom_article"
							class="form-control" required />
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
						<label for="prix" class="form-label">Mise à prix : </label><input type="number" id="prix" name="prix_initial" min="0"
							value="${ param.prix_initial }" class="form-control" required>
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="debut" class="form-label">Début de l'enchère : </label> <input id="debut" type="date" name="date_debut_encheres"
							value="${ param.date_debut_encheres }" class="form-control" required />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="fin" class="form-label">Fin de l'enchère : </label> <input id="fin" type="date" name="date_fin_encheres"
							value="${ param.date_fin_encheres }" class="form-control" required />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">

						<fieldset>

							<legend>Retrait</legend>

							<p>
								<label for="rue" class="form-label">Rue : </label> <input id="rue" type="text" value="${ profilUtilisateur.rue }" name="rue"
									class="form-control" required />
							</p>

							<p>
								<label for="code_postal" class="form-label">Code postal : </label> <input id="code_postal" type="text"
									value="${ profilUtilisateur.codePostal }" name="code_postal" class="form-control" required />
							</p>

							<p>
								<label for="ville" class="form-label">Ville : </label> <input id="ville" type="text" value="${ profilUtilisateur.ville }" name="ville"
									class="form-control" required />
							</p>

						</fieldset>
					</div>

					<div class="row">
						<div class="col">
							<button class="btn btn-primary ml-3" name="enregistrer">Enregistrer</button>
						</div>
					</div>


				</form>
			</div>

		</div>
	</div>

	<%@ include file="footer.jspf"%>

</body>
</html>