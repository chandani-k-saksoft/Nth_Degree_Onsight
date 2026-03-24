package utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportHelper {

    public static String extractBody(Document doc) throws Exception {
        String body = "";

        Element scenarioCard = doc.select("div.card:has(h6.card-title:contains(Scenarios))").first();
        if (scenarioCard != null) {
            String passedText = scenarioCard.select("div.card-footer").text();
            int passed = extractCount(passedText, "scenarios passed");
            int failed = extractCount(passedText, "scenarios failed");
            int totals = passed + failed;
            double pass_per = ((double) passed / totals) * 100;
            double fail_per = ((double) failed / totals) * 100;

            String[] time = getDuration(doc).split("_");

            System.out.println("Scenarios Summary:");
            System.out.println("Passed : " + passed);
            System.out.println("Failed : " + failed);
            System.out.println("Passed %: " + String.format("%.2f%%", pass_per));
            System.out.println("Failed % : " + String.format("%.2f%%", fail_per));
            System.out.println("Start Time : " + time[0]);
            System.out.println("End Time : " + time[1]);
            System.out.println("Duration : " + time[2] + ":" + time[3] + ":" + time[4]);

            StringBuilder failedTC_Str = new StringBuilder("""
                    <br>
                        <table border="1" cellpadding="2" cellspacing="0" style="border-collapse:collapse; font-family:Arial,sans-serif;">
                            <tr style="background-color:#f2f2f2;">
                                <th colspan="2" style="color:#333; font-size: 18px; padding: 0px; text-align:center;">Failed test cases:</th>
                            </tr>
                            <tr style="background-color:#f2f2f2;">
                                <th style="color:#333; font-size: 14px; padding: 0px; text-align:center;">&nbsp;Index&nbsp;</th>
                                <th style="color:#333; font-size: 14px; padding: 0px; text-align:center;">Test Title</th>
                            </tr>""");

            List<String> failedTC_List = getFailedTestCase(doc);
            for (String abc : failedTC_List)
                System.out.println(abc);
            for (String failTC : failedTC_List)
                failedTC_Str.append(String.format("""
                        <tr>
                            <td style="background-color:#f2f2f2; text-align:center;">%d</td>
                            <td style="background-color:#e3f2fd;">%s</td>
                        </tr>""", (failedTC_List.indexOf(failTC) + 1), failTC));
            failedTC_Str.append("</table>");

            body = body + """
                        <html>
                        <body>
                            Hi All,<br><br>
                            Please find below the summary of the recent test execution:<br><br>
                            <table border="1" cellpadding="9" cellspacing="0" style="border-collapse:collapse; font-family:Arial,sans-serif; text-align:center;">
                                <tr style="background-color:#f2f2f2;">
                                    <th colspan="9" style="color:#333; font-size: 18px; padding: 0px;">Test Execution Summary</th>
                                </tr>
                                <tr style="background-color:#f2f2f2;">
                                    <th style="color:#333;">Executed Scenarios</th>
                                    <th style="color:#333;">Total Scenarios</th>
                                    <th style="color:#2e7d32;">Passed</th>
                                    <th style="color:#c62828;">Failed</th>
                                    <th style="color:#2e7d32;">Passed %</th>
                                    <th style="color:#c62828;">Failed %</th>
                                    <th style="color:#0277bd;">Start Time</th>
                                    <th style="color:#0277bd;">End Time</th>
                                    <th style="color:#0277bd;">Duration</th>
                                </tr>
                                <tr>
                                    <td style="background-color:#fff59d;">{TAGS}</td> <!-- Executed Scenarios -->
                                    <td style="background-color:#e3f2fd;">{TOTAL}</td> <!-- Total Scenarios -->
                                    <td style="background-color:#c8e6c9;">{PASSED}</td> <!-- Passed -->
                                    <td style="background-color:#ffcdd2;">{FAILED}</td> <!-- Failed -->
                                    <td style="background-color:#c8e6c9;">{PASS_PER}</td> <!-- Pass % -->
                                    <td style="background-color:#ffcdd2;">{FAIL_PER}</td> <!-- Fail % -->
                                    <td style="background-color:#b3e5fc;">{START_TIME}</td> <!-- Start Time -->
                                    <td style="background-color:#b2dfdb;">{END_TIME}</td> <!-- End Time -->
                                    <td style="background-color:#dcedc8;">{DURATION}</td> <!-- Duration -->
                                </tr>
                            </table>
                            <br>
                            {FAILED_TC_STR}
                            <br>The attached report provides detailed insights into the failed scenario for further analysis. Kindly review it and let us know if any additional information is required.<br>
                            <br>Please let me know if you'd like to schedule a discussion to review the failure or plan the next steps.
                            <br><br>Best regards,<br>Automation<br></body></html>
                    """;

            String tags = System.getProperty("cucumber.filter.tags") != null ? System.getProperty("cucumber.filter.tags") : "null";

            body = body.replace("{TAGS}", tags).replace("{TOTAL}", String.valueOf(totals));
            body = body.replace("{PASSED}", String.valueOf(passed)).replace("{FAILED}", String.valueOf(failed));
            body = body.replace("{PASS_PER}", String.format("%.2f%%", pass_per)).replace("{FAIL_PER}", String.format("%.2f%%", fail_per));
            body = body.replace("{START_TIME}", String.valueOf(time[0])).replace("{END_TIME}", String.valueOf(time[1]));
            body = body.replace("{DURATION}", time[2] + ":" + time[3] + ":" + time[4]);

            if (failedTC_List.isEmpty())
                body = body.replace("{FAILED_TC_STR}", "");
            else
                body = body.replace("{FAILED_TC_STR}", failedTC_Str);

        } else {
            return "<html><body>Report not generated successfully</body></html>";
        }
        return body;
    }

    public static List<String> getFailedTestCase(Document doc) {
        List<String> failedTC = new ArrayList<>();
        Map<String, Integer> counter = new HashMap<>();

        Elements spans = doc.select("div.card-title span[class*=fail-bg]");
        for (Element span : spans) {
            Element parent = span.parent();
            String parentText = parent.text().trim();
            parentText = parentText.replace("Re-Executed", "").replace("Fail", "").trim();
            int count = counter.getOrDefault(parentText, 0) + 1;
            counter.put(parentText, count);

            if (count == 1)
                failedTC.add(parentText); // first time, keep original
            else
                failedTC.add(parentText + " (" + (count - 1) + ")");
        }
        return failedTC;
    }

    public static void filterTags(Document doc, String filePath) throws Exception {

        Element tagsTable = doc.select("table.table-bordered").first();
        Map<String, List<Integer>> scenariosByFeature = scenariosByFeature(doc);

        if (tagsTable != null) {
            Elements cols = tagsTable.select("thead > tr > th");
            cols.get(0).text("NAME");
            cols.get(1).text("TOTAL");
            cols.get(2).text("PASSED");
            cols.get(3).text("FAILED");
            if (cols.size() > 4)
                cols.get(4).remove();

            Elements rows = tagsTable.select("tbody > tr");

            for (Element row : rows)
                row.remove();

            for (Map.Entry<String, List<Integer>> entry : scenariosByFeature.entrySet()) {
                Element newRow = new Element(Tag.valueOf("tr"), "");
                newRow.appendChild(new Element(Tag.valueOf("td"), "").text(entry.getKey()));
                for (Integer val : entry.getValue())
                    newRow.appendChild(new Element(Tag.valueOf("td"), "").text(String.valueOf(val)));
                double percentage = ((double) entry.getValue().get(1) / entry.getValue().get(0)) * 100;
                newRow.appendChild(new Element(Tag.valueOf("td"), "").text((int) percentage + "%"));
                tagsTable.appendChild(newRow);
            }
        }

        FileWriter writer = new FileWriter(filePath);
        writer.write(doc.outerHtml());
        writer.close();
        System.out.println("Filtered report created successfully.");

    }

    public static Document updateExtentReportHtml(Document doc) {
        // Step 0: Remove unused views
        doc.select("li[onclick=\"toggleView('exception-view')\"]").remove();
        doc.select("li[onclick=\"toggleView('category-view')\"]").remove();
        doc.select(".main-content>.category-view").remove();
        doc.select(".main-content>.exception-view").remove();

        Elements testItems = doc.select("li.test-item");
        Map<String, List<Element>> featureMap = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> featureScenarioCounts = new LinkedHashMap<>();
        Map<String, List<Element>> imageMap = new LinkedHashMap<>();

        // Step 1: Group features by name
        for (Element test : testItems) {
            String name = test.select("p.name").text().trim();
            featureMap.computeIfAbsent(name, k -> new ArrayList<>()).add(test);
        }

        for (Map.Entry<String, List<Element>> entry : featureMap.entrySet()) {
            List<Element> features = entry.getValue();
            if (features.size() <= 1) continue;

            Element firstFeature = features.get(0);
            Element firstAccordion = firstFeature.select(".accordion").first();

            Map<String, Element> scenarioMap = new LinkedHashMap<>();

            for (Element feature : features) {
                Element accordion = feature.select(".accordion").first();
                if (accordion == null) continue;

                Elements cards = accordion.select(".card");
                for (Element card : cards) {
                    Element node = card.select(".card-title .node").first();
                    Element badge = card.select(".card-title .badge").first();
                    Element image = card.select(".card-body a").first();
                    Element errorBlock = card.select(".card-body .code-block").first();

                    if (node == null || badge == null) continue;

                    String scenarioName = node.ownText().trim();

                    // ✅ Store first screenshot/error for each scenario
                    if (image != null && !imageMap.containsKey(scenarioName)) {
                        imageMap.put(scenarioName, Arrays.asList(
                                image.clone(),
                                errorBlock != null ? errorBlock.clone() : null
                        ));
                    }

                    // ✅ If scenario already exists, means re-executed
                    if (!scenarioMap.containsKey(scenarioName)) {
                        scenarioMap.put(scenarioName, card.clone());
                    } else {
                        Element existing = scenarioMap.get(scenarioName);
                        boolean existingIsPass = existing.select(".card-title .badge").first().className().contains("pass-bg");
                        boolean existingIsFail = existing.select(".card-title .badge").first().className().contains("fail-bg");

                        if (existingIsPass || existingIsFail) {
                            // ✅ Keep the latest card (re-executed version)
                            Element retriedCard = card.clone();

                            // Add "Re-Executed" badge
                            Element retryBadge = new Element(Tag.valueOf("span"), "")
                                    .attr("style", "background-color: #ff9800 !important; color: white;")
                                    .addClass("badge log retry-badge scenario mr-2")
                                    .text("Re-Executed");
                            retriedCard.select(".card-title .node").first().prependChild(retryBadge);

                            // ✅ Attach previous screenshot/error to this new card
                            List<Element> prevAssets = imageMap.get(scenarioName);
                            if (prevAssets != null) {
                                Element prevImage = prevAssets.get(0) != null ? prevAssets.get(0).clone() : null;
                                Element prevError = prevAssets.get(1) != null ? prevAssets.get(1).clone() : null;

                                if (prevImage != null) {
                                    prevImage.select("span").first().text("Failed_Previous");

                                    Element divWrapper = new Element(Tag.valueOf("div"), "").addClass("row mb-3");
                                    Element divCol = new Element(Tag.valueOf("div"), "").addClass("col-md-3");
                                    divCol.appendChild(prevImage);
                                    divWrapper.appendChild(divCol);

                                    Element divError = new Element(Tag.valueOf("div"), "").addClass("fail-bg");
                                    divError.text("Previous Execution:");
                                    if (prevError != null) divError.appendChild(prevError);

                                    Element stepContainer = retriedCard.select(".card-body .step").last();
                                    if (stepContainer != null) {
                                        stepContainer.appendChild(divWrapper);
                                        stepContainer.appendChild(divError);
                                    }
                                }
                            }

                            // ✅ Replace old scenario with re-executed one (remove duplicates)
                            scenarioMap.put(scenarioName, retriedCard);
                        }
                    }
                }
            }

            // Step 2: Replace accordion content with merged scenarios
            firstAccordion.empty();
            for (Element scenarioCard : scenarioMap.values()) {
                firstAccordion.appendChild(scenarioCard);
            }

            // Step 3: Remove duplicate feature nodes (keeping first)
            for (int i = 1; i < features.size(); i++) {
                features.get(i).remove();
            }
        }

        // Step 4: Fix inconsistent badge labels
        Elements nodes = doc.select("div.node");
        for (Element node : nodes) {
            Element badge = node.select("span.badge").first();
            if (badge != null) {
                String label = badge.text().trim();
                String badgeClass = badge.className();
                if (badgeClass.contains("pass-bg") && label.equalsIgnoreCase("Fail")) {
                    badge.text("Pass");
                    node.text("Pass");
                } else if (badgeClass.contains("fail-bg") && label.equalsIgnoreCase("Pass")) {
                    badge.text("Fail");
                    node.text("Fail");
                }
            }
        }

        // Step 5: Count and update summary stats
        int feature_pass = 0;
        int feature_fail = 0;
        int scenario_fail = 0;
        int scenario_pass = 0;

        testItems = doc.select("li.test-item[test-id]");
        for (Element test : testItems) {
            String name = test.select("p.name").text().trim();
            featureMap.computeIfAbsent(name, k -> new ArrayList<>()).add(test);

            Elements totalScenarios = test.select(".card");
            Elements scenarioBadges = test.select(".accordion .card .badge");
            Map<String, Integer> counts = new HashMap<>();
            counts.put("total_sc", totalScenarios.size());
            counts.put("Passed", 0);
            counts.put("Failed", 0);
            counts.put("Retry", 0);

            for (Element badge : scenarioBadges) {
                String cls = badge.className();
                if (cls.contains("pass-bg")) counts.put("Passed", counts.get("Passed") + 1);
                else if (cls.contains("fail-bg")) counts.put("Failed", counts.get("Failed") + 1);
                else if (cls.contains("retry-badge")) counts.put("Retry", counts.get("Retry") + 1);
            }

            featureScenarioCounts.put(name, counts);
            if (Objects.equals(counts.get("total_sc"), counts.get("Passed"))) {
                Element badge = test.select(".badge").first();
                if (badge.className().contains("fail-bg")) {
                    badge.removeClass("fail-bg").addClass("pass-bg").text("Pass");
                }
                test.select(".test-item").attr("status", "pass");
                feature_pass++;
            } else feature_fail++;

            if (counts.get("Retry") > 0) {
                Element retriedCard = test.select(".badge").first().parent();
                Element retryBadge = new Element(Tag.valueOf("span"), "")
                        .attr("style", "background-color: #ff9800 !important; color: white;")
                        .addClass("badge log retry-badge feature float-right mr-2")
                        .text("Re-Executed");
                retriedCard.appendChild(retryBadge);
            }

            scenario_pass += counts.get("Passed");
            scenario_fail += counts.get("Failed");
        }

        // Step 6: Update footer summary
        Elements numbers = doc.select("div.card-footer b");
        if (numbers.size() >= 6) {
            numbers.get(0).text(String.valueOf(feature_pass));
            numbers.get(1).text(String.valueOf(feature_fail));
            numbers.get(4).text(String.valueOf(scenario_pass));
            numbers.get(5).text(String.valueOf(scenario_fail));
        }

        // Step 7: Update chart + filters
        doc = updateFilterType(updateChart(doc));
        return doc;
    }

    public static File getUniqueFile(String folderPath, String baseName, String extension) {
        int index = 0;
        File file;
        do {
            String fileName = (index == 0)
                    ? baseName + extension
                    : baseName + "_" + index + extension;
            file = new File(folderPath, fileName);
            index++;
        } while (file.exists());
        return file;
    }

    private static int extractCount(String text, String label) {
        try {
            String regex = "(\\d+)\\s+" + label;
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            // Ignore and default to 0
        }
        return 0;
    }

    private static String getDuration(Document doc) throws Exception {
        Element startElem = doc.select("div.card>div.card-body:has(p:contains(Started))>h3").first();
        Element endElem = doc.select("div.card>div.card-body:has(p:contains(Ended))>h3").first();
        String time = "";
        if (startElem != null && endElem != null) {
            String startText = startElem.text();
            String endText = endElem.text();

            startText = startText.replace("Sept", "Sep");
            endText = endText.replace("Sept", "Sep");

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
            Date startDate = sdf.parse(startText);
            Date endDate = sdf.parse(endText);


            long diffInMillis = endDate.getTime() - startDate.getTime();
            long seconds = (diffInMillis / 1000) % 60;
            long minutes = (diffInMillis / (1000 * 60)) % 60;
            long hours = (diffInMillis / (1000 * 60 * 60)) % 24;

            time = String.format("%s_%s_%02d_%02d_%02d", startText, endText, hours, minutes, seconds);
        } else {
            time = "-_-_-_-_-";
        }
        return time;
    }

    private static Document updateFilterType(Document doc) {

        Element ul_elements = doc.select("#status-toggle").first();


        Element a_element = new Element(Tag.valueOf("a"), "")
                .attr("status", "reset")
                .attr("href", "#")
                .addClass("dropdown-item");
        Element span_element = new Element(Tag.valueOf("span"), "")
                .text("Re-Executed");
        Element icon_element = new Element(Tag.valueOf("span"), "")
                .addClass("status warning");
        a_element.appendChild(span_element);
        a_element.appendChild(icon_element);
        ul_elements.appendChild(a_element);
        return doc;
    }

    private static Document updateChart(Document doc) {

        Elements featureElements = doc.select("li.test-item");
        int totalFeatures = featureElements.size();
        int passedFeatures = 0;
        int failedFeatures = 0;

        for (Element feature : featureElements) {
            String status = feature.attr("status").toLowerCase();
            if (status.equals("pass")) passedFeatures++;
            else if (status.equals("fail")) failedFeatures++;
        }

        doc.select("div.card>div.card-body:has(p:contains(Features Failed))>h3").first().text(String.valueOf(failedFeatures));

        // ==== COUNT SCENARIOS ====
        Elements scenarioNodes = doc.select("div.node");
        int totalScenarios = 0;
        int passedScenarios = 0;
        int failedScenarios = 0;

        for (Element node : scenarioNodes) {
            totalScenarios++;

            if (!node.select(".pass-bg").isEmpty()) {
                passedScenarios++;
            } else if (!node.select(".fail-bg").isEmpty()) {
                failedScenarios++;
            }
        }

        // ==== Update Feature Chart Footer ====
        Element featureFooter = doc.select(".card:has(canvas#parent-analysis) .card-footer").first();
        doc.select(".card:has(canvas#parent-analysis) .card-title").first().text("Features Status");
        int featurePassPct = (int) Math.round((passedFeatures * 100.0) / totalFeatures);
        if (featureFooter != null) {
            featureFooter.html(String.format("""
                    <div><small><b>%d</b> Total features</small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> features passed </small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> features failed</small></div>
                    """, (passedFeatures + failedFeatures), featurePassPct, passedFeatures, (100 - featurePassPct), failedFeatures));
        }

        // ==== Update Scenario Chart Footer ====
        Element scenarioFooter = doc.select(".card:has(canvas#child-analysis) .card-footer").first();
        doc.select(".card:has(canvas#child-analysis) .card-title").first().text("Scenarios Status");
        int scenarioPassPct = (int) Math.round((passedScenarios * 100.0) / totalScenarios);
        if (scenarioFooter != null) {
            scenarioFooter.html(String.format("""
                    <div><small><b>%d</b> Totals scenarios</small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> scenarios passed</small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> scenarios failed</small></div>
                    """, (passedScenarios + failedScenarios), scenarioPassPct, passedScenarios, (100 - scenarioPassPct), failedScenarios));
        }

        int reExecutedCount = doc.select("span.badge.retry-badge:not(.feature)").size();
        int reExecutedPassCount = doc.select(".node:has(span.badge.retry-badge:not(.feature)) span.pass-bg").size();
        int reExecutedFailCount = doc.select(".node:has(span.badge.retry-badge:not(.feature)) span.fail-bg").size();

        doc.select(".card:has(canvas#grandchild-analysis) .card-title").first().text("Failed Scenario Re-Execution Status");
        Element stepsFooter = doc.select(".card:has(canvas#grandchild-analysis) .card-footer").first();
        int reExecutedPassPct = (int) Math.round((reExecutedPassCount * 100.0) / reExecutedCount);

        if (stepsFooter != null) {
            stepsFooter.html(String.format("""
                    <div><small><b>%d</b> total scenarios re-executed</small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> scenarios passed</small></div>
                    <div><small data-tooltip="%d%%"><b>%d</b> scenarios failed</small></div>
                    """, (reExecutedPassCount + reExecutedFailCount), reExecutedPassPct, reExecutedPassCount, (100 - reExecutedPassPct), reExecutedFailCount));
        }


        // ==== Update statusGroup JavaScript ====
        for (Element script : doc.getElementsByTag("script")) {
            if (script.data().contains("var statusGroup")) {
                script.html(String.format("""
                                var statusGroup = {
                                  parentCount: %d,
                                  passParent: %d,
                                  failParent: %d,
                                  warningParent: 0,
                                  skipParent: 0,
                                  childCount: %d,
                                  passChild: %d,
                                  failChild: %d,
                                  warningChild: 0,
                                  skipChild: 0,
                                  infoChild: 0,
                                  grandChildCount: %d,
                                  passGrandChild: %d,
                                  failGrandChild: %d,
                                  warningGrandChild: 0,
                                  skipGrandChild: 0,
                                  infoGrandChild: 0,
                                  eventsCount: %d,
                                };
                                """,
                        totalFeatures, passedFeatures, failedFeatures,
                        totalScenarios, passedScenarios, failedScenarios,
                        reExecutedCount, reExecutedPassCount, reExecutedFailCount, totalScenarios
                ));
            }
        }

        return doc;
    }

    private static Map<String, List<Integer>> scenariosByFeature(Document doc) {
        Elements featureElements = doc.select("li.test-item[test-id]");
        Map<String, List<Integer>> featureScenarioCountMap = new LinkedHashMap<>();
        Map<String, List<Integer>> featureScenarioCountMap_final = new LinkedHashMap<>();
        for (Element featureElement : featureElements) {
            String featureName = featureElement.select("p.name").text().trim();
            int passCount = 0;
            int failCount = 0;

            Elements scenarioNodes = featureElement.select("div.node");
            for (Element scenario : scenarioNodes) {
                String status = scenario.select("span.badge").text().trim().toLowerCase(); // "pass", "fail", etc.
                if (status.contains("pass")) {
                    passCount++;
                } else if (status.contains("fail")) {
                    failCount++;
                }
            }

            featureScenarioCountMap.put(featureName, Arrays.asList(passCount, failCount));
        }

        // Print the result
        for (Map.Entry<String, List<Integer>> entry : featureScenarioCountMap.entrySet())
            featureScenarioCountMap_final.put("@" + entry.getKey().replace("Scenarios", "").replace(" ", ""), Arrays.asList((entry.getValue().get(0) + entry.getValue().get(1)), entry.getValue().get(0), entry.getValue().get(1)));

        for (Map.Entry<String, List<Integer>> entry : featureScenarioCountMap_final.entrySet())
            System.out.println("TAG: " + entry.getKey() + "\n\tTotal: " + entry.getValue().get(0) + ", Pass: " + entry.getValue().get(1) + ", Fail: " + entry.getValue().get(2) + "\n");
        return featureScenarioCountMap_final;
    }
}