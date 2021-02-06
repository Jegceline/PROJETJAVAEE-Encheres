<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Accueil</title>

<%@ include file="jspf/css.jspf"%>

</head>

<body class="body-flex">

	<%@ include file="jspf/header.jspf"%>

	<div class="content">

		<div class="container pt-5 mb-4">

			<h2 class="my-4">Liste des enchères</h2>
			<hr>
			
			<!-- DEBUG
			<c:if test="${trieur.ventesUtilisateurAttente == 'ventes_attente'}">checked</c:if> 
			-->
			
			<!--  div à l'affichage conditionnel -->
			
			<c:if test="${not empty param.succesSuppressionArticle}"> <!-- attribut envoyé par la servlet VenteArticle -->
				<div class="alert alert-success" role="alert">${param.succesSuppressionArticle}</div>
			</c:if>
			
			<c:if test="${not empty param.succesModificationsArticle}"> <!-- attribut envoyé par la servlet VenteArticle -->
				<div class="alert alert-success" role="alert">${param.succesModificationsArticle}</div>
			</c:if>

			<c:if test="${ not empty param.succesEnchere}"> <!-- attribut envoyé par la servlet DetailArticle -->
				<div class="alert alert-success" role="alert">${param.succesEnchere}</div>
			</c:if>
			
			<c:if test="${ not empty param.succesInscription}"> <!-- attribut envoyé par la servlet Inscription -->
				<div class="alert alert-success" role="alert">${param.succesInscription}</div>
			</c:if>

			<c:if test="${ not empty param.succesAjoutVente}"> <!-- attribut envoyé par la servlet VenteArticle -->
				<div class="alert alert-success" role="alert">${param.succesAjoutVente}</div>
			</c:if>
			
			<c:if test="${ not empty param.succesSupressionUtilisateur}"> <!-- attribut envoyé par la servlet ModifierProfil -->
				<div class="alert alert-success" role="alert">${param.succesSupressionUtilisateur}</div>
			</c:if>
			
			<!--
			<c:forEach var="couple" items="${ requestScope.mapErreurs }"> 
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>
			-->
			
			<!-- Fin des div à afficage conditionnel -->


			<h4 class="my-4">Filtres</h4>

			<form action="<%=request.getContextPath()%>/accueil" method="POST">

				<div class="row my-4">
					<div class="col">
						<label for="keyword" class="form-label">Mot clé : </label> <input id="keyword" type="text" value="" name="keyword" class="form-control"
							placeholder="Le nom de l'article contient" />
					</div>
				</div>

				<div class="row">
					<div class="col-sm">
						<%@ include file="jspf/liste-categories.jspf"%>
					</div>
				</div>

<!-- La div suivante ne s'affiche que s'il existe un attribut profilUtilisateur dans la session -->

				<c:if test="${ not empty sessionScope.profilUtilisateur }">

					<div class="row">

						<div class="col">

							<div class="form-check mt-4 ml-4">

								<p>
									<input type="radio" id="achats" name="type" value="achats" class="form-check-input" disabled> <label for="achats"
										class="form-check-label">Achats</label>
								</p>

								<p>
									<input type="checkbox" class="form-check-input" id="achats1" name="encheres_ec" value="encheres_ec"><label for="encheres_ec"
										class="form-check-label">enchères en cours</label>
								</p>
								<p>
									<input type="checkbox" class="form-check-input" id="achats2" name="mes_encheres" value="mes_encheres"> <label for="mes_encheres"
										class="form-check-label">mes enchères en cours</label>
								</p>
								<p>
									<input type="checkbox" class="form-check-input" id="achats3" name="mes_encheres_remportees" value="mes_encheres_remportees"> <label
										for="mes_encheres_remportees" class="form-check-label">mes enchères remportées</label>
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
									<input type="checkbox" class="form-check-input" id="ventes1" name="ventes_attente" value="ventes_attente"> <label
										for="ventes_attente" class="form-check-label">ventes à venir</label>
								</p>
								<p>
									<input type="checkbox" class="form-check-input" id="ventes2" name="ventes_ouvertes" value="ventes_ouvertes"> <label
										for="ventes_ouvertes" class="form-check-label">ventes en cours</label>
								</p>
								<p>
									<input type="checkbox" class="form-check-input" id="ventes3" name="ventes_terminees" value="ventes_terminees"> <label
										for="ventes_terminees" class="form-check-label">ventes terminées</label>
								</p>
							</div>
						</div>

						<hr>

					</div>
				</c:if>
				
<!-- fin de la div à l'affichage conditionnée par l'existence d'un attribut profilUtilisateur dans la session -->

		<div class="col">
			<button class="btn btn-primary mx-auto my-4 d-block" name="rechercher">Rechercher</button>
		</div>
		
		</form>
	</div>

	<section class="container my-4">
	
	<!-- la div suivante ne s'affiche que s'il existe un attribut noResult dans un des objets de contexte -->
		<c:if test="${ not empty requestScope.noResult}">
			<div class="alert alert-warning" role="alert">${noResult}</div>
		</c:if>
	<!-- fin de la div à l'affichage conditionnel -->

		<div class="card-columns my-4">

<!-- La div suivante ne s'affiche que s'il n'existe pas d'attribut selectionArticles dans l'un des objets de contexte -->

			<c:choose>
				<c:when test="${ empty requestScope.selectionArticles }">

					<c:forEach var="article" items="${encheresOuvertes}">

						<div class="card bg-light my-4" style="width: 22rem;">

							<div class="card-header">
								<h5 class="card-title">${article.nomArticle}</h5>
							</div>

							<div class="card-body">
								<p class="card-text">Prix de vente initial : <b>${article.prixInitial} points</b></p>
								<p class="card-text">Début des enchères : <b>${article.dateDebutEncheres}</b> (${article.heureDebutEncheres})</p>
								<p class="card-text">Clôture des enchères : <b>${article.dateFinEncheres}</b> (${article.heureFinEncheres})</p>
								<p class="card-text">Vendeur : <b>${article.vendeur.pseudo}</b></p>
								<p class="card-text">Dernière enchère : <b>${article.prixVente} points (${article.dernierEncherisseur.pseudo})</b></p>
								<a href="<%=request.getContextPath()%>/membre/fiche-article?noArticle=${article.noArticle}" class="btn btn-info">Plus de détails</a>
							</div>
						</div>

					</c:forEach>

				</c:when>

				<c:when test="${ not empty requestScope.selectionArticles }">

					<c:forEach var="article" items="${selectionArticles}">

						<div class="card bg-light my-4" style="width: 22rem;">

							<div class="card-header">
								<h5 class="card-title">${article.nomArticle}</h5>
							</div>

							<div class="card-body">
								<p class="card-text">Prix de vente initial : <b>${article.prixInitial} points</b></p>
								<p class="card-text">Début des enchères : <b>${article.dateDebutEncheres}</b> (${article.heureDebutEncheres})</p>
								<p class="card-text">Clôture des enchères : <b>${article.dateFinEncheres}</b> (${article.heureFinEncheres})</p>
								<p class="card-text">Vendeur : <b>${article.vendeur.pseudo}</b></p>
								<p class="card-text">Dernière enchère : <b>${article.prixVente} points (${article.dernierEncherisseur.pseudo})</b></p>
								<a href="<%=request.getContextPath()%>/membre/fiche-article?noArticle=${article.noArticle}" class="btn btn-info">Plus de détails</a>
							</div>
						</div>

					</c:forEach>
				</c:when>
			
			</c:choose>
			
<!-- fin de la div à l'affichage conditionnel -->

		</div>

	</section>

	</div>
	

	<%@ include file="jspf/footer.jspf"%>


</body>
</html>