<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Détail vente</title>

<%@ include file="css.jspf"%>

</head>

<body class="body-flex">

	<%@ include file="header.jspf"%>

	<div class="content">
		<div class="container my-4 pt-5">


			<h2 class="my-4">Fiche Article</h2>
			<hr>

			<c:if test="${ not empty succesEnchere}">
				<div class="alert alert-success" role="alert">${succesEnchere}</div>
			</c:if>

			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>

			<div class="row">

				<div class="col">
					<img src="img_girl.jpg" class="img-thumbnail img-fluid" alt="Photo de l'objet" style="width: 500px; height: 600px;">
				</div>

				<div class="col">
					<form action="detail-article" method="POST">

						<div class="mb-3">
							<input id="titre" type="text" value="${articleSelectionne.nomArticle }" name="libelle" class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="pseudo">Description : </label>
							<textarea id="description" name="description" rows="4" cols="50" class="form-control" disabled>${articleSelectionne.description }</textarea>

						</div>

						<div class="mb-3">
							<label for="vendeur">Vendeur : </label> <input id="vendeur" type="text" name="vendeur" value="${articleSelectionne.vendeur.pseudo}"
								class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="retrait">Adresse de retrait : </label> <input id="retrait" type="text" name="retrait"
								value="${articleSelectionne.adresseRetrait.rue}, ${articleSelectionne.adresseRetrait.codePostal} ${articleSelectionne.adresseRetrait.ville}"
								class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="prix_initial">Mise à prix : </label> <input id="prix_initial" type="text" name="prix_initial"
								value="${articleSelectionne.prixInitial}" class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="meilleure_offre">Meilleure offre : </label> <input id="meilleure_offre" type="text" name="meilleure_offre"
								value="${articleSelectionne.prixVente}" class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="date_fin_encheres">Fin des enchères :</label> <input id="date_fin_encheres" type="text" name="date_fin_encheres"
								value="${articleSelectionne.dateFinEncheres}" class="form-control" disabled />
						</div>

						<div class="mb-3">
							<label for="enchere_prix">Mon enchère :</label> <input id="enchere_prix" type="number" name="enchere_prix" class="form-control" />
						</div>

						<div class="mb-3">
							<button class="btn btn-success " name="encherir">Enchérir</button>

							<c:if test="${ profilUtilisateur.noUtilisateur == articleSelectionne.vendeur.noUtilisateur}">
								<button class="btn btn-success ml-4" name="modifier">Modifier</button>
							</c:if>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>

	<%@ include file="footer.jspf"%>


</body>
</html>