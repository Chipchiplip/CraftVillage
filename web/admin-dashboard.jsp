<%@page import="java.util.Calendar"%>
<%@page import="java.util.Map"%>
<%@page import="java.math.BigDecimal"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>
    </head>
    <body class="bg-gray-100">
        <div class="flex h-screen">
            <!-- Sidebar -->
            <jsp:include page="admin-sidebar.jsp"></jsp:include>
            <%
                Map<Integer, BigDecimal> revenue = (Map<Integer, BigDecimal>) request.getAttribute("revenueMap");
                StringBuilder revenueData = new StringBuilder("[");
                for (int i = 0; i < 12; i++) {
                    BigDecimal value = (revenue != null && revenue.get(i) != null) ? revenue.get(i) : BigDecimal.ZERO;
                    revenueData.append(value).append(i < 11 ? "," : "");
                }
                revenueData.append("]");
            %>
            <%
                Map<Integer, Integer> monthlyRegister = (Map<Integer, Integer>) request.getAttribute("monthlyRegister");
                int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth();

                StringBuilder accountLabels = new StringBuilder("[");
                StringBuilder accountData = new StringBuilder("[");
                for (int day = 1; day <= daysInMonth; day++) {
                    accountLabels.append("'").append(String.format("%02d", day)).append("/").append(String.format("%02d", month)).append("',");
                    int count = (monthlyRegister != null && monthlyRegister.containsKey(day)) ? monthlyRegister.get(day) : 0;
                    accountData.append(count).append(",");
                }
                if (accountLabels.length() > 1) {
                    accountLabels.setLength(accountLabels.length() - 1);
                }
                if (accountData.length() > 1) {
                    accountData.setLength(accountData.length() - 1);
                }
                accountLabels.append("]");
                accountData.append("]");
            %>
            <%
                Map<Integer, Integer> statusOrder = (Map<Integer, Integer>) request.getAttribute("statusOrder");
                int processing = statusOrder != null && statusOrder.containsKey(0) ? statusOrder.get(0) : 0;
                int paid = statusOrder != null && statusOrder.containsKey(1) ? statusOrder.get(1) : 0;
                int cancelled = statusOrder != null && statusOrder.containsKey(2) ? statusOrder.get(2) : 0;
                int refunded = statusOrder != null && statusOrder.containsKey(3) ? statusOrder.get(3) : 0;
            %>
            <!-- Main content -->
            <div class="w-5/6 p-6 overflow-y-auto">
                <!-- Statistic Cards -->
                <div class="grid grid-cols-3 gap-4 mb-6">
                    <div class="bg-white p-4 rounded shadow">
                        <h3 class="text-gray-700">Account currently accessing</h3>
                        <p class="text-2xl font-bold text-blue-600">${activeSessions}/${accountCount.size()}</p>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <h3 class="text-gray-700">New product orders</h3>
                        <p class="text-2xl font-bold text-green-600">${currentProductPost}</p>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <h3 class="text-gray-700">New ticket orders</h3>
                        <p class="text-2xl font-bold text-green-600">${currentTicketPost}</p>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <h3 class="text-gray-700">New craft village post</h3>
                        <p class="text-2xl font-bold text-indigo-600">${currentPost}</p>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <h3 class="text-gray-700">Today's Revenue</h3>
                        <p class="text-2xl font-bold text-red-600">${currentRevenue}₫</p>
                    </div>
                </div>

                <!-- Charts -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <!-- Revenue Chart -->
                    <div class="bg-white p-6 rounded shadow flex flex-col justify-between col-span-1 h-72">
                        <h4 class="text-center font-semibold text-gray-700 mb-4">Revenue</h4>
                        <div class="h-full">
                            <canvas id="barChart" class="w-full h-full"></canvas>
                        </div>
                    </div>

                    <!-- Account Registration Chart -->
                    <div class="bg-white p-6 rounded shadow flex flex-col justify-between col-span-1 h-72">
                        <h4 class="text-center font-semibold text-gray-700 mb-4">New account register</h4>
                        <div class="h-full">
                            <canvas id="lineChart" class="w-full h-full"></canvas>
                        </div>
                    </div>

                    <!-- Order Status Chart with custom legend -->
                    <div class="bg-white p-6 rounded shadow flex flex-col justify-between col-span-1 h-72">
                        <h4 class="text-center font-semibold text-gray-700 mb-4">Order status in current month</h4>
                        <div class="flex flex-1 items-center justify-center gap-10">
                            <div class="flex-1 flex justify-center items-center h-full">
                                <div class="relative w-full h-full">
                                    <canvas id="pieChart" class="w-full h-full"></canvas>
                                </div>
                            </div>

                            <!-- Custom legend -->
                            <div class="space-y-2 text-sm flex-1">
                                <div class="flex items-left">
                                    <span class="w-4 h-4 bg-yellow-400 rounded mr-2"></span>
                                    Processing: <strong class="ml-1 text-gray-800"><%= processing%></strong>
                                </div>
                                <div class="flex items-left">
                                    <span class="w-4 h-4 bg-green-500 rounded mr-2"></span>
                                    Paid: <strong class="ml-1 text-gray-800"><%= paid%></strong>
                                </div>
                                <div class="flex items-left">
                                    <span class="w-4 h-4 bg-red-500 rounded mr-2"></span>
                                    Canceled: <strong class="ml-1 text-gray-800"><%= cancelled%></strong>
                                </div>
                                <div class="flex items-left">
                                    <span class="w-4 h-4 bg-indigo-500 rounded mr-2"></span>
                                    Refund: <strong class="ml-1 text-gray-800"><%= refunded%></strong>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script>
            // Revenue Bar Chart
            const revenueLabels = ['January', 'February', 'March', 'April', 'May', 'June',
                'July', 'August', 'September', 'October', 'November', 'December'];
            const revenueData = <%= revenueData.toString()%>;

            new Chart(document.getElementById('barChart'), {
                type: 'bar',
                data: {
                    labels: revenueLabels,
                    datasets: [{
                            label: 'Revenue (dong)',
                            data: revenueData,
                            backgroundColor: 'rgba(59, 130, 246, 0.7)'
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'dong'
                            }
                        }
                    }
                }
            });

            // Account Line Chart
            const accountLabels = <%= accountLabels.toString()%>;
            const accountData = <%= accountData.toString()%>;

            new Chart(document.getElementById('lineChart'), {
                type: 'line',
                data: {
                    labels: accountLabels,
                    datasets: [{
                            label: 'Account registered by date',
                            data: accountData,
                            borderColor: 'rgba(16, 185, 129, 1)',
                            backgroundColor: 'rgba(16, 185, 129, 0.1)',
                            tension: 0.4,
                            fill: true,
                            pointRadius: 4
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Account number'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Day of the month'
                            }
                        }
                    }
                }
            });

            // Pie Chart - Order Status
            const pieLabels = ['Processing', 'Paid', 'Canceled', 'Refund'];
            const pieData = [<%= processing%>, <%= paid%>, <%= cancelled%>, <%= refunded%>];

            new Chart(document.getElementById('pieChart'), {
                type: 'pie',
                data: {
                    labels: pieLabels,
                    datasets: [{
                            data: pieData,
                            backgroundColor: ['#FBBF24', '#10B981', '#EF4444', '#6366F1']
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false, // rất quan trọng để phù hợp kích thước container
                    plugins: {
                        legend: {
                            display: false
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    const label = context.label || '';
                                    const value = context.raw || 0;
                                    return `${label}: ${value} orders`;
                                }
                            }
                        }
                    }
                }
            });
        </script>
    </body>
</html>
