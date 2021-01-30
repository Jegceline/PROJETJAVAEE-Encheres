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

	<div class="container pt-5 mb-4">

		<div class="container alert alert-${ alertMessage } ">

			${succesEnchere}

			<c:forEach var="couple" items="${ mapErreurs }">
				<p style="color: red;">${couple.value}</p>
			</c:forEach>
		</div>

		<h2 class="my-4">Liste des enchères</h2>

		<h4 class="my-4">Filtres</h4>

		<form action="" method="POST">

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


					<input type="radio" id="achats" name="type" value="achats"> <label for="achats" class="form-check-label">Achats</label>

					<div class="form-check">

						<p>
							<input type="checkbox" class="form-check-input" id="achats1" name="type" value="encheres_ouvertes" checked> <label for="achats1"
								class="form-check-label">enchères en cours</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="achats2" name="achats" value="mes_encheres" disabled> <label for="achats2"
								class="form-check-label">mes enchères en cours</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="achats3" name="achats" value="mes_encheres_remportees" disabled> <label
								for="achats3" class="form-check-label">mes enchères remportées</label>
						</p>
					</div>

				</div>

				<div class="col">

					<div class="form-check">
						<p>
							<input type="radio" class="form-check-input" id="ventes" name="type" value="ventes"> <label for="ventes">Mes ventes</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes1" name="achats" value="encheres_ouvertes"> <label for="ventes1">enchères
								à venir</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes2" name="achats" value="mes_encheres" checked> <label for="ventes2">enchères
								en cours</label>
						</p>
						<p>
							<input type="checkbox" class="form-check-input" id="ventes3" name="achats" value="mes_encheres_remportees"> <label for="ventes3">enchères
								terminée</label>
						</p>
					</div>
				</div>

				<div class="col">
					<button class="btn btn-success" name="rechercher">Rechercher</button>
				</div>

			</div>


		</form>

		<section class="container pt-5">

			<h4 class="my-4">Enchères ouvertes</h4>

			<div class="row">

				<c:choose>
					<c:when test="${ empty articlesFiltres }">

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
										<a href="<%=request.getContextPath()%>/detail-article?noArticle=${article.noArticle}" class="card-link">Plus de détails</a>
									</div>
								</div>
							</div>

						</c:forEach>

					</c:when>

					<c:when test="${ not empty articlesFiltres }">

						<c:forEach var="article" items="${articlesFiltres}">

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
										<a href="<%=request.getContextPath()%>/detail-article?noArticle=${article.noArticle}" class="card-link">Plus de détails</a>

									</div>
								</div>
							</div>

						</c:forEach>
					</c:when>

				</c:choose>

			</div>

		</section>
		
			</div>
		
			<%@ include file="footer.jspf"%>


</body>
</html>