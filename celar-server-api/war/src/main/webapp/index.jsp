<html>
<head>
<title>CELAR Server</title>
<style>
table
{
border-collapse: collapse;
border: 1px solid black;
}
th
{
width:400px;
border:1px solid black;
}
td
{
border-left:1px solid black;
}
</style>
</head>
<body>
<h2>CELAR Server</h2>
<p>This page contains a list of the available API calls accessed by the CELAR Server. This list will be refreshed gradually, so stay tuned :).</p>
<table>
<tr>
<th style='width:50px'>No</th>
<th>Call Name</th>
<th>Status</th>
<th>URI</th>
</tr>
<tr>
<% String uri="/celar-server-api/application/describe/";%>
<td>1</td>
<td><a href="<%=uri%>?info">Describe Application</a></td>
<td>DONE</td>
<td><%=uri%></td>
</tr>
<tr>
<% uri="/celar-server-api/deployment/deploy/";%>
<td>2</td>
<td><a href="<%=uri%>?info">Deploy Application</a></td>
<td>DONE</td>
<td><%=uri%></td>
</tr>
</table>
</body>
</html>