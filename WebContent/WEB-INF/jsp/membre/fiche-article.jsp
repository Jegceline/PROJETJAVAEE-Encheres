<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Détail vente</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">

</head>

<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>

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

					<form action="<%=request.getContextPath()%>/membre/fiche-article" method="POST">

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
								value="${articleSelectionne.dateFinEncheres} (${articleSelectionne.heureFinEncheres})" class="form-control" disabled />
						</div>

						<!-- le champ enchérir ne doit pas s'afficher si l'article est vendu par l'utilisateur -->
						<!-- il ne doit également pas s'afficher si les enchères sont clôturées sur un article dont il n'est pas le vendeur -->
						
						<c:if test="${ sessionScope.profilUtilisateur.noUtilisateur != articleSelectionne.vendeur.noUtilisateur && empty encheresCloturees}">
							<div class="mb-3">
								<label for="enchere_prix">Mon enchère :</label> <input id="enchere_prix" type="number" name="enchere_prix" class="form-control" />
							</div>
						</c:if>

						<div class="mb-3">

							<!-- le bouton enchérir ne doit pas s'afficher si l'article est vendu par l'utilisateur -->
							<!-- il ne doit également pas s'afficher si les enchères sont clôturées sur un article dont il n'est pas le vendeur -->
							
							<c:if test="${ sessionScope.profilUtilisateur.noUtilisateur != articleSelectionne.vendeur.noUtilisateur && empty encheresCloturees}">
								<button class="btn btn-success" name="encherir">Enchérir</button>
							</c:if>

							<!-- le bouton Modifier ne doit pas s'afficher si l'utilisateur n'est pas le vendeur de l'article ou si les enchères sont ouvertes ou clôturées -->
							
							<c:if test="${ sessionScope.profilUtilisateur.noUtilisateur == articleSelectionne.vendeur.noUtilisateur && empty encheresOuvertes && empty encheresCloturees}">
								<button class="btn btn-info ml-4" name="modifier">Modifier</button>
							</c:if>

						</div>
					</form>

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="../accueil">Accueil</a></li>
							<li class="breadcrumb-item active" aria-current="page">Fiche Article</li>
						</ol>
					</nav>
				</div>
			</div>

		</div>
	</div>

	<%@ include file="../jspf/footer.jspf"%>


</body>
</html>