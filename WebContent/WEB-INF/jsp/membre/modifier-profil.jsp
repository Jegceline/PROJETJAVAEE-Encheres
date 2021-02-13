<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier mes informations</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">

</head>
<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>
	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="mt-4">Modifier mon profil</h2>
			<hr>


			<c:forEach var="couple" items="${ mapErreurs }">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>


			<form action="<%=request.getContextPath()%>/membre/modifier-profil" method="POST">

				<div class="row my-2">
					<div class="col">
						<label for="pseudo">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo" class="form-control"
							required />
					</div>

					<div class="col">
						<label for="nom">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom" class="form-control" required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">

						<label for="prenom">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom" class="form-control"
							required />
					</div>

					<div class="col">
						<label for="email">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email" class="form-control"
							required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="telephone">Téléphone : </label> <input id="telephone" type="text" name="telephone" value="${ profilUtilisateur.telephone }"
							class="form-control" />
					</div>

					<div class="col">
						<label for="rue">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }" class="form-control" required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="code_postal">Code postal :</label> <input id="code_postal" type="text" name="codePostal" value="${ profilUtilisateur.codePostal }"
							class="form-control" required />
					</div>

					<div class="col">
						<label for="telephone">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }" class="form-control"
							required />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="mot_de_passe">Mot de passe : </label> <input id="mot_de_passe" type="password" name="motDePasse"
							value="${ profilUtilisateur.motDePasse}" class="form-control" required />
					</div>

					<div class="col">
						<label for="mot_de_passe">Confirmation : </label> <input id="mot_de_passe" type="password" name="motDePasseBis"
							value="${ profilUtilisateur.motDePasse}" class="form-control" required />
					</div>
				</div>

				<div class="row my-4">
					<div class="col">
						<button class="btn btn-info" name="enregistrer">Enregistrer les modifications</button>
						<button class="btn ml-4 btn-danger" name="supprimer">Supprimer mon compte</button>
					</div>
				</div>
			</form>

			<div>
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/accueil">Accueil</a></li>
						<li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/membre/profil">Profil</a></li>
						<li class="breadcrumb-item active" aria-current="page">Modifier mon profil</li>
					</ol>
				</nav>
			</div>
		</div>
	</div>

	<%@ include file="../jspf/footer.jspf"%>

</body>
</html>