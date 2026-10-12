<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Verified Submission - Summary App</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
  <main class="card">
    <h1>Verified Submission</h1>
    <div class="summary">
      <p>Name: <c:out value="${submission.name}"/></p>
      <p>Email: <c:out value="${submission.email}"/></p>
      <p>Summary:</p>
      <p class="summary-text"><c:out value="${submission.summary}"/></p>
    </div>
    <a class="button" href="/about.html">Start Over</a>
  </main>
</body>
</html>
