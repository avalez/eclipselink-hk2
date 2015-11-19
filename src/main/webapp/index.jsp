<%@ page import="weblogic.invocation.ComponentInvocationContextManager" %>
<%@ page import="weblogic.invocation.ComponentInvocationContext" %>
<%@ page import="weblogic.invocation.PartitionTable" %>
<%@ page import="weblogic.invocation.PartitionTableEntry" %>
<%@ page import="javax.inject.Inject" %>
<%@ page import="org.glassfish.hk2.api.ServiceLocator" %>
<%@ page import="weblogic.server.GlobalServiceLocator" %>
<html>
<head>
    <title>Partition Reporter</title>
    <style type='text/css'>
        .repBody h1 {
            background: #0fffff
        }

        .repBody h2 {
            background: #afffff
        }

        .repTable {
            font-size: 14px;
            color: #333333;
            width: 90%;
            border-width: 1px;
            border-color: #a9a9a9;
            border-collapse: collapse;
        }

        .repTable tr {
            background-color: #ffffff;
        }

        .repTable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #a9a9a9;
        }
    </style>
</head>


<body class="repBody">


<h1 align="center">Partition Reporter</h1>

<h2>Request Info</h2>
<table class="repTable" border="1">
    <tr>
        <td>RequestURL</td>
        <td><%= request.getRequestURL() %>
        </td>
    </tr>
</table>
<%
    Integer visitCountInteger = (Integer) session.getAttribute("visitCount");
    int visitCount = (visitCountInteger == null ? 0 : visitCountInteger.intValue());
    visitCount++;
    session.setAttribute("visitCount", visitCount);
%>
<h2>Session Info</h2>

<p>Visited this page <b><%= visitCount %>
</b> times in this session.</p>
<%
    ComponentInvocationContextManager manager = ComponentInvocationContextManager.getInstance();
    ComponentInvocationContext context = manager.getCurrentComponentInvocationContext();
    ServiceLocator locator = GlobalServiceLocator.getServiceLocator();
%>
<h2>Partition Info</h2>
<%
    if (context == null) {
%>
<p>None available.</p>
<%
} else {
%>
<table class="repTable" border="1">
    <%
        String uuid = null;
        String partitionName = null;

        try {
            uuid = context.getPartitionId();
            partitionName = context.getPartitionName();

            if (uuid == null || uuid.trim().length() <= 0) {
                System.out.println("No UUID in CIC -- getting from PartitionTable...");
                // not in the CIC -- let's work with the global info...
                PartitionTable pt = PartitionTable.getInstance();
                PartitionTableEntry pte = pt.lookupByName(partitionName);
                uuid = pte.getPartitionID();
            }
        }
        catch(Exception e) {
	        System.out.println("CATCH!!!!!!");
            uuid = null;
        }
        if (uuid != null && uuid.trim().length() > 0) {
            System.out.println("UUID = " + uuid);
    %>
    <tr>
        <td>PartitionId</td>
        <td><%= uuid %>
        </td>
    </tr>
    <%
        }
    %>
    <tr>
        <td>PartitionName</td>
        <td><%= partitionName %>
        </td>
    </tr>
    <tr>
        <td>ApplicationId</td>
        <td><%= context.getApplicationId() %>
        </td>
    </tr>
    <tr>
        <td>ApplicationName</td>
        <td><%= context.getApplicationName() %>
        </td>
    </tr>
    <tr>
        <td>ApplicationVersion</td>
        <td><%= context.getApplicationVersion() %>
        </td>
    </tr>
    <tr>
        <td>ModuleName</td>
        <td><%= context.getModuleName() %>
        </td>
    </tr>
    <tr>
        <td>ComponentName</td>
        <td><%= context.getComponentName() %>
        </td>
    </tr>
</table>
<%
    }
%>
</body>
</html>
