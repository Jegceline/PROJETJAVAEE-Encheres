<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Détail vente</title>

<%@ include file="css.jspf"%>
</head>
<body>

	<%@ include file="header.jspf"%>

	<div class="container pt-5">

		${succesEnchere}

		<c:forEach var="couple" items="${ mapErreurs }">
			<p style="color: red;">${couple.value}</p>
		</c:forEach>

		<div class="row">

			<div class="col">
				<img src="img_girl.jpg" class="img-thumbnail img-fluid" alt="Photo de l'objet" style="width: 500px; height: 600px;">
			</div>

			<div class="col">
				<form action="detail-article" method="POST">

					<div class="mb-3 col-sm-12 col-xs-12">
						<input id="titre" type="text" value="${articleSelectionne.nomArticle }" name="libelle" class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="pseudo">Description : </label> <input id="description" type="text" name="description" value="${articleSelectionne.description }"
							class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="categorie">Catégorie : </label> <input id="categorie" type="text" value="" name="categorie"
							value="${articleSelectionne.noCategorie} " class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="vendeur">Vendeur : </label> <input id="vendeur" type="text" name="vendeur" value="${articleSelectionne.pseudoVendeur}"
							class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="retrait">Adresse de retrait : </label> <input id="retrait" type="text" name="retrait" value="${articleSelectionne.adresseRetrait.rue}, ${articleSelectionne.adresseRetrait.codePostal} ${articleSelectionne.adresseRetrait.ville}"
							class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="prix_initial">Mise à prix : </label> <input id="prix_initial" type="text" name="prix_initial"
							value="${articleSelectionne.prixInitial}" class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="meilleure_offre">Meilleure offre : </label> <input id="meilleure_offre" type="text" name="meilleure_offre"
							value="${articleSelectionne.prixVente}" class="form-control" disabled />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<label for="enchere_prix">Mon enchère :</label> <input id="enchere_prix" type="number" name="enchere_prix" class="form-control" />
					</div>

					<div class="mb-3 col-sm-12 col-xs-12">
						<button class="btn btn-success " name="encherir">Enchérir</button>
					</div>
				</form>
			</div>

		</div>

	</div>

</body>
</html>