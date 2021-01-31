<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Accueil</title>

<%@ include file="css.jspf"%>

</head>

<body class="body-flex">

	<%@ include file="header.jspf"%>

	<div class="content">

		<div class="container pt-5 mb-4">


			<h2 class="my-4">Liste des enchères</h2>
			<hr>

			<c:if test="${ not empty succesEnchere}">
				<div class="alert alert-success" role="alert">${succesEnchere}</div>
			</c:if>


			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>


			<h4 class="my-4">Filtres</h4>

			<form action="accueil" method="POST">

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

						<div class="form-check mt-4 ml-4">

							<p>
								<input type="radio" id="achats" name="type" value="achats" class="form-check-input" disabled> <label for="achats"
									class="form-check-label">Achats</label>
							</p>

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

						<div class="form-check mt-4">

							<p>
								<input type="radio" class="form-check-input" id="ventes" name="type" value="ventes" disabled> <label for="ventes"
									class="form-check-label">Mes ventes</label>
							</p>

							<p>
								<input type="checkbox" class="form-check-input" id="ventes1" name="achats" value="encheres_ouvertes" disabled> <label for="ventes1"
									class="form-check-label">enchères à venir</label>
							</p>
							<p>
								<input type="checkbox" class="form-check-input" id="ventes2" name="achats" value="mes_encheres" disabled> <label for="ventes2"
									class="form-check-label">enchères en cours</label>
							</p>
							<p>
								<input type="checkbox" class="form-check-input" id="ventes3" name="achats" value="mes_encheres_remportees" disabled> <label
									for="ventes3" class="form-check-label">enchères terminées</label>
							</p>
						</div>
					</div>

					<div class="col">
						<button class="btn btn-primary mx-auto d-block" name="rechercher">Rechercher</button>
					</div>

				</div>

			</form>



			<section class="container">

				<hr>
				<h4 class="mb-4">Enchères ouvertes</h4>

				<hr>

				<div class="card-columns">

					<c:choose>
						<c:when test="${ empty articlesFiltres }">

							<c:forEach var="article" items="${encheresEC}">

								<div class="card bg-light mb-3" style="width: 18rem;">

									<div class="card-header">
										<h5 class="card-title">${article.nomArticle}</h5>
									</div>

									<div class="card-body">
										<p class="card-text">Prix de vente initial : ${article.prixInitial} points</p>
										<p class="card-text">Dernière enchère : ${article.prixVente} points</p>
										<p class="card-text">Clôture des enchères : ${article.dateFinEncheres}</p>
										<p class="card-text">Vendeur : ${article.pseudoVendeur}</p>

										<a href="<%=request.getContextPath()%>/detail-article?noArticle=${article.noArticle}" class="btn btn-info">Plus de détails</a>
									</div>
								</div>

							</c:forEach>

						</c:when>

						<c:when test="${ not empty articlesFiltres }">

							<c:forEach var="article" items="${articlesFiltres}">

								<div class="card bg-light mb-3" style="width: 18rem;">

									<div class="card-header">
										<h5 class="card-title">${article.nomArticle}</h5>
									</div>

									<div class="card-body">
										<p class="card-text">Prix de vente initial : ${article.prixInitial} points</p>
										<p class="card-text">Dernière enchère : ${article.prixVente} points</p>
										<p class="card-text">Clôture des enchères : ${article.dateFinEncheres}</p>
										<p class="card-text">Vendeur : ${article.pseudoVendeur}</p>

										<a href="<%=request.getContextPath()%>/detail-article?noArticle=${article.noArticle}" class="btn btn-info">Plus de détails</a>
									</div>
								</div>

							</c:forEach>
						</c:when>

					</c:choose>

				</div>

			</section>

		</div>
	</div>

	<%@ include file="footer.jspf"%>


</body>
</html>