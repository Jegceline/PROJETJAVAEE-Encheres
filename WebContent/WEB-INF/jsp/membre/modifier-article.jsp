<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier un article</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">
</head>

<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="my-4">Modifier un article</h2>
			<hr>

			<!--  div à l'affichage conditionnel -->

			<c:if test="${not empty venteEnCours}">
				<!-- attribut envoyé par la servlet ModifierArticle -->
				<!-- permet de gérer le cas où l'utilisateur modifie l'URL en mettant le numéro d'un article en cours de vente -->
				<div class="alert alert-danger" role="alert">${venteEnCours}</div>
			</c:if>

			<c:forEach var="couple" items="${mapErreurs}">
				<!-- attribut envoyé par la servlet ModifierArticle -->
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>

			<!-- Fin des div à affichage conditionnel -->

			<form action="<%=request.getContextPath()%>/membre/modifier-article" method="POST">

				<div class="row">

					<div class="col">

						<label for="libelle" class="form-label my-2">Article : </label>
						<input id="libelle" type="text" value="${ param.nom_article } ${article.nomArticle}" name="nom_article" class="form-control" required 
						<c:if test="${not empty venteEnCours}">disabled</c:if> /> 
						
						<label for="description" class="form-label my-2">Description : </label>
						<textarea id="description" cols="50" name="description" rows="4" cols="50" class="form-control" maxlength="300" placeholder="Pas plus de 300 caractères" required 
						<c:if test="${not empty venteEnCours}">disabled</c:if>>${ param.description } ${article.description}</textarea>

						<%@ include file="../jspf/liste-categories.jspf"%>

						<label for="photo" class="form-label">Photo de l'article : </label> <input id="photo" type="file" value="" name="photo" class="form-control"
							disabled />

					</div>

					<div class="col">

						<label for="prix" class="form-label my-2">Mise à prix : </label>
						<input type="number" id="prix" name="prix_initial" min="0"
							value="${ param.prix_initial }${article.prixInitial}" class="form-control" <c:if test="${not empty venteEnCours}">disabled</c:if> required>
						
						<label for="debut" class="form-label my-2">Début de l'enchère : </label> 
						<input id="debut" type="date" name="date_debut_encheres" value="${param.date_debut_encheres}${article.dateDebutEncheres}" class="form-control" required
						<c:if test="${not empty venteEnCours}">disabled</c:if> /> 
							
						<label for="debut" class="form-label my-2">Heure : </label> 
						<input id="debut" type="time" name="heure_debut_encheres" value="${param.heure_debut_encheres}${article.heureDebutEncheres}" class="form-control"
						<c:if test="${not empty venteEnCours}">disabled</c:if> /> 
							
						<label for="fin" class="form-label my-2">Fin de l'enchère : </label> 
						<input id="fin" type="date" name="date_fin_encheres" value="${param.date_fin_encheres}${article.dateFinEncheres}" class="form-control" required
						<c:if test="${not empty venteEnCours}">disabled</c:if> /> 
							
						<label for="debut" class="form-label my-2">Heure : </label> 
						<input id="debut" type="time" name="heure_fin_encheres" value="${param.heure_fin_encheres}${article.heureFinEncheres}" class="form-control"
						<c:if test="${not empty venteEnCours}">disabled</c:if> />


					</div>
				</div>


				<div class="row">

					<div class="col">

						<fieldset class="border p-2 my-4">

							<legend class="w-auto">Retrait</legend>

							<p>
								<label for="rue" class="form-label">Rue : </label> 
								<input id="rue" type="text" value="<c:if test="${empty article}">${ profilUtilisateur.rue}</c:if> ${article.adresseRetrait.rue}" name="rue" class="form-control"
								<c:if test="${not empty venteEnCours}">disabled</c:if> required />
							</p>

							<p>
								<label for="code_postal" class="form-label">Code postal : </label> 
								<input id="code_postal" type="text" value="<c:if test="${ empty article}">${ profilUtilisateur.codePostal } </c:if>${article.adresseRetrait.codePostal}" name="code_postal"
									class="form-control" <c:if test="${not empty venteEnCours}">disabled</c:if> required />
							</p>

							<p>
								<label for="ville" class="form-label">Ville : </label> 
								<input id="ville" type="text" value="<c:if test="${ empty article}">${ profilUtilisateur.ville } </c:if> ${article.adresseRetrait.ville}" name="ville" class="form-control"
								<c:if test="${not empty venteEnCours}">disabled</c:if> required />
							</p>

						</fieldset>
					</div>

					<div class="col"></div>
				</div>

				<div class="row">
					<div class="col">

						<!-- ces deux boutons ne s'affichent que si l'article n'est pas en cours de vente -->

						<c:if test="${empty venteEnCours}">
							<button class="btn btn-info ml-3" name="enregistrerModifications">Enregistrer les modifications</button>
							<button class="btn btn-danger ml-3" name="supprimer">Supprimer</button>
						</c:if>

					</div>

					<div class="col"></div>

				</div>

			</form>
			
			<div class="my-4">
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/accueil">Accueil</a></li>
						<li class="breadcrumb-item active" aria-current="page">Vendre un article</li>
					</ol>
				</nav>
			</div>

		</div>
		

	</div>


	<%@ include file="../jspf/footer.jspf"%>

</body>
</html>>