<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Accueil</title>

<%@ include file="css.jspf"%>

</head>
<body>

	<%@ include file="header.jspf"%>

	<div class="container pt-5">

		<div class="container alert alert-${ alertMessage } ">

			<c:forEach var="couple" items="${ mapErreurs }">
				<p style="color: red;">${couple.value}</p>
			</c:forEach>
		</div>

		<h2 class="my-4">Liste des enchères</h2>

		<form action="" method="POST">

			<h4 class="my-4">Filtres</h4>

			<div class="row my-4">
				<div class="col">
					<label for="keyword" class="form-label">Mot clé : </label> <input id="keyword" type="text" value="" name="keyword" class="form-control"
						placeholder="Le nom de l'article contient" />
				</div>
			</div>

			<div class="row">
				<div class="col-sm">
					<%@ include file="listeCategories.jspf"%>
				</div>
			</div>

			<div class="row">
				<div class="col">

					<input type="radio" id="achats" name="achats" value="achats" disabled> <label for="achats" class="form-check-label">Achats</label>

					<div class="form-check">

						<input type="checkbox" class="form-check-input" id="achats1" name="achats" value="encheres_ouvertes" checked> <label for="achats1"
							class="form-check-label">enchères ouvertes</label> <input type="checkbox" class="form-check-input" id="achats2" name="achats"
							value="mes_encheres" disabled> <label for="achats2" class="form-check-label">mes enchères</label> <input type="checkbox"
							class="form-check-input" id="achats3" name="achats" value="mes_encheres_remportees" disabled> <label for="achats3"
							class="form-check-label">mes enchères remportées</label>
					</div>

				</div>

				<div class="col">

					<div class="form-check">
						<p>
							<input type="radio" class="form-check-input" id="ventes" name="ventes" value="ventes" disabled> <label for="ventes">Mes ventes</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes1" name="achats" value="encheres_ouvertes" disabled> <label for="ventes1">enchères
								ouvertes</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes2" name="achats" value="mes_encheres" disabled> <label for="ventes2">mes
								enchères</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes3" name="achats" value="mes_encheres_remportees" disabled> <label
								for="ventes3">mes enchères remportées</label>
						</p>
					</div>
				</div>

				<div class="col">
					<button class="btn btn-success" name="rechercher">Rechercher</button>
				</div>

			</div>


	</form>

	</div>

	<section class="container pt-5">

		<h4 class="my-4">Enchères ouvertes</h4>

		<div class="row">

			<c:forEach var="article" items="${encheresEC}">

				<div class="col-sm">
					<div class="card" style="width: 18rem;">

						<div class="card-body">
							<h5 class="card-title">${article.nomArticle}</h5>
						</div>

						<ul class="list-group list-group-flush">
							<li class="list-group-item">Prix : ${article.prixVente} points</li>
							<li class="list-group-item">Fin de l'enchère : ${article.dateFinEncheres}</li>
							<li class="list-group-item">Vendeur : ${article.pseudoVendeur}</li>
						</ul>

						<div class="card-body">
							<a href="detail-article" class="card-link">Plus de détails</a>
						</div>
					</div>
				</div>

			</c:forEach>

		</div>

	</section>

</body>
</html>