/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package servlet.stateless;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;

// Though it is perfectly fine to declare the dependency on the bean
// at the type level, it is not required for stateless session bean
// Hence the next two lines are commented and we rely on the
// container to inject the bean.
// @EJB(name="StatelessSession", beanInterface=StatelessSession.class)
@WebServlet(name = "Servlet", urlPatterns = {"/servlet"})
public class ServletStateless extends HttpServlet {

    @Resource(name = "coneccion")
    private DataSource ds;

    // Using injection for Stateless session bean is still thread-safe since
    // the ejb container will route every request to different
    // bean instances. However, for Stateful session beans the
    // dependency on the bean must be declared at the type level
//    @EJB
//    private StatelessSessionBean sless;
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        // <editor-fold defaultstate="collapsed" desc="servlet original">
////        if (p.isEmpty()) {
////            resp.setContentType("text/html");
////            PrintWriter out = resp.getWriter();
////            try {
////                out.println("<HTML> <HEAD> <TITLE> Servlet Output </TITLE> </HEAD> <BODY BGCOLOR=white>");
////                out.println("<CENTER> <FONT size=+1> SYLLABUS:: Parametros Incorrectos </FONT> </CENTER> <p> ");
////                out.println("<form method=\"POST\">");
////                out.println("<TABLE>");
////                out.println("<tr><td> No existen registros con estos parametros !! </td>");
//////                out.println("<td><input type=\"text\" name=\"name\"> </td>");
////                out.println("</tr><tr><td></td>");
//////                out.println("<td><input type=\"submit\" name=\"sub\"> </td>");
////                out.println("</tr>");
////                out.println("</TABLE>");
////                out.println("</form>");
//////                    String val = req.getParameter("name");
////
//////                    if ((val != null) && (val.trim().length() > 0)) {
//////                        out.println("<FONT size=+1 color=red> Greeting from StatelessSessionBean: </FONT>"
//////                                    + HTMLFilter.filter(sless.sayHello(val)) + "<br>");
//////                    }
////                out.println("</BODY> </HTML> ");
////
////            } catch (Exception ex) {
////                ex.printStackTrace();
////                System.out.println("webclient servlet test failed");
////                throw new ServletException(ex);
////            }
////        } else {
////
////            String parAnio = req.getParameter("anio");
////            String parAsignatura = req.getParameter("asig");
////            String parDocente = req.getParameter("doc");
////            String parPeriodo = req.getParameter("per");
////            if ((parAnio != null) && (parAnio.trim().length() > 0)) {
////                parAnio = HTMLFilter.filter(parAnio);
////            }
////            if ((parAsignatura != null) && (parAsignatura.trim().length() > 0)) {
////                parAsignatura = HTMLFilter.filter(parAsignatura);
////            }
////            if ((parDocente != null) && (parDocente.trim().length() > 0)) {
////                parDocente = HTMLFilter.filter(parDocente);
////            }
////            if ((parPeriodo != null) && (parPeriodo.trim().length() > 0)) {
////                parPeriodo = HTMLFilter.filter(parPeriodo);
////            }
////
////            ServletOutputStream servletOutputStream = resp.getOutputStream();
////            File reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus.jasper"));
////            byte[] bytes = null;
////            Connection con = null;
////            Map params = new HashMap();
////            try {
////                con = ds.getConnection();
////                params.put("anio", Long.parseLong(parAnio));
////                params.put("asig", Long.parseLong(parAsignatura));
////                params.put("per", Long.parseLong(parPeriodo));
////                bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), params, con);
////////                esta linea es para descargar el reporte
//////                resp.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
////                resp.setHeader("Content-Disposition", "inline; filename=\"reporte.pdf\";");
////                resp.setContentType("application/pdf");
////                resp.setContentLength(bytes.length);
////
////                servletOutputStream.write(bytes, 0, bytes.length);
////                servletOutputStream.flush();
////                servletOutputStream.close();
////            } catch (JRException e) {
////                // display stack trace in the browser
////                StringWriter stringWriter = new StringWriter();
////                PrintWriter printWriter = new PrintWriter(stringWriter);
////                e.printStackTrace(printWriter);
////                resp.setContentType("text/plain");
////                resp.getOutputStream().print(stringWriter.toString());
////            } catch (SQLException ex) {
////                Logger.getLogger(ServletStateless.class.getName()).log(Level.SEVERE, null, ex);
////            } finally {
////                params = null;
////                bytes = null;
////                try {
////                    con.close();
////                } catch (SQLException ex) {
////                    Logger.getLogger(ServletStateless.class.getName()).log(Level.SEVERE, null, ex);
////                }
////            }
////        }
        // </editor-fold> 
        if (req.getParameterMap().isEmpty()) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            try {
                out.println("<HTML> <HEAD> <TITLE> Servlet Output </TITLE> </HEAD> <BODY BGCOLOR=white>");
                out.println("<CENTER> <FONT size=+1> REPORTE:: Parametros Incorrectos </FONT> </CENTER> <p> ");
                out.println("<form method=\"POST\">");
                out.println("<TABLE>");
                out.println("<tr><td> No puedo generar un reporte sin parametros !! </td>");
                out.println("</tr><tr><td></td>");
                out.println("</tr>");
                out.println("</TABLE>");
                out.println("</form>");
                out.println("</BODY> </HTML> ");
            } catch (Exception ex) {
                throw new ServletException(ex);
            } finally {
                out.close();
            }
        } else {
            String urlContextoReporte = null;
            String contentType = null;
            String titulo = null;
            Map<String, Object> params = new HashMap<String, Object>();

            /* nombre del archivo generado */
            titulo = (req.getParameter("titulo") == null ? "document" : req.getParameter("titulo"));
            /* si no existe el tipo, el reporte debe generar en formato PDF por defecto */
            contentType = (req.getParameter("tipo") == null ? "application/pdf" : "application/" + req.getParameter("tipo"));

            for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                if (entry.getKey().equals("pfdrid_c") || entry.getKey().equals("tipo") || entry.getKey().equals("contexto") || entry.getKey().equals("reporte")) {
                    continue;
                }
                params.put(entry.getKey(), entry.getValue()[0]);
            }

//            System.out.println("pOpGraficos: "+req.getParameter("pOpGraficos"));
            if (req.getParameter("pOpGraficos") != null) {
                List<String> opGraficos = new ArrayList<String>(Arrays.asList(req.getParameter("pOpGraficos").replace("[", "").replace("]", "").replace(", ", ",").split(",")));
                params.put("pOpGraficos", opGraficos);
            }

//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                System.out.println(key + ": " + value.toString());
//            }

            /* si no existe el contexto, debe salir un mensaje de reporte no encontrado*/
            if (req.getParameter("contexto") == null && req.getParameter("reporte") == null) {
                /* si no existe el reporte y el contexto, se imprime el silabus pór defecto en formato PDF por defecto */
                File reportFile;
                String anio = (params.get("anio") == null ? null : params.get("anio").toString());  // si existe anio, tomarlo para control posterior
//                if (new Long(anio) >= new Long("2014")) {
//                    reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus2014.jasper"));
//                } else {
//                    reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus.jasper"));
//                }
                if (new Long(anio) > new Long("2019")) {
                    reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus2019.jasper"));
                } else if (new Long(anio) >= new Long("2014") && new Long(anio) <= new Long("2019")) {
                    reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus2014.jasper"));
                } else {
                    reportFile = new File(getServletConfig().getServletContext().getRealPath("/rep/Syllabus.jasper"));
                }
                urlContextoReporte = reportFile.getPath();
            } else if (req.getParameter("contexto") != null && req.getParameter("reporte") != null) {
////                    urlContextoReporte = req.getParameter("contexto") + "/" + req.getParameter("reporte") + ".jasper";
                String urlContextoAplicacion = getServletConfig().getServletContext().getRealPath("/");
                String replace = urlContextoAplicacion.replace("ServletImpresion", req.getParameter("contexto"));
                urlContextoReporte = replace + "reportes/" + req.getParameter("reporte") + ".jasper";
            } else if (req.getParameter("contexto") == null && req.getParameter("reporte") != null) {
                // mensaje de error
                return;
            }
//            System.out.println("urlContextoReporte "+urlContextoReporte);
            genReporte(urlContextoReporte, params, contentType, req, resp, titulo);
            params = null;
        }
    }

    private void genReporte(String urlContextoReporte, Map<String, Object> params, String contentType,
            HttpServletRequest req, HttpServletResponse resp, String titulo) throws IOException {
        JasperPrint jasperPrint = null;
        JRExporter exporter = null;
        Connection con = null;
//        /* System.out.println(Arrays.asList(GraphicsEnvironment
//         .getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));*/

        ServletOutputStream out = resp.getOutputStream();
        try {
            con = ds.getConnection();
            jasperPrint = JasperFillManager.fillReport(urlContextoReporte, params, con);
            if ("application/pdf".equals(contentType)) {
                exporter = new JRPdfExporter();
                exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, out);
                resp.setContentType(contentType);
                resp.setHeader("Content-Disposition", "inline; filename=\"" + titulo + ".pdf\"");
            } else if ("text/html".equals(contentType)) {
                exporter = new JRHtmlExporter();
                exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRHtmlExporterParameter.OUTPUT_WRITER, resp.getWriter());
//                FacesContext.getCurrentInstance().getExternalContext().getRequest();
                //req.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, req.getContextPath() + "/image?image=");
                resp.setContentType(contentType);
            } else if ("application/vnd.ms-excel".equals(contentType)) {
                exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out);
                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//                exporter.setParameter(JRXlsExporterParameter.PASSWORD, "ua13sb20");
                resp.setContentType("application/xls");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + titulo + ".xls\"");
            } else if ("application/vnd.ms-word".equals(contentType)) {
                exporter = new JRDocxExporter();
                exporter.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, out);
                resp.setContentType("application/docx");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + titulo + ".docx\"");
            }
            if (exporter != null) {
                exporter.exportReport();
            }
//            out.flush();
//            out.close();
        } catch (JRException ex) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            resp.setContentType("text/plain");
            out.print("ERROR " + stringWriter.toString());
        } catch (IOException ex) {
        } catch (RuntimeException e) {

        } catch (SQLException sqlex) {
            Logger.getLogger(ServletStateless.class.getName()).log(Level.SEVERE, null, sqlex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    System.out.println("ServletImpresion XXX");
                }
            }
            try {
                exporter = null;
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServletStateless.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
