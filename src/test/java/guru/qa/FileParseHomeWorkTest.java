package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileParseHomeWorkTest {

    ClassLoader cl = FileParseTest.class.getClassLoader();

    @DisplayName("PDF from Zip")
    @Test
    void pdfFromZipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) !=null) {
            if (entry.getName().contains("SM_godovoy_otchet.pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(pdf.text).contains("Добро пожаловать домой!");
                }
            }
        }
    }

    @DisplayName("XLS from Zip")
    @Test
    void xlsxFromZipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) !=null) {
            if (entry.getName().contains("TeamRoster.xlsx")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    assertThat(
                            xls.excel.getSheetAt(0)
                                    .getRow(1)
                                    .getCell(2)
                                    .getStringCellValue()
                    ).isEqualTo("JONATHAN HUBERDEAU");

                }
            }
        }
    }

    @DisplayName("CSV from Zip")
    @Test
    void csvFromZipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) !=null) {
            if (entry.getName().contains("TeamRosterCSV.csv")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                    List<String[]> content = reader.readAll();
                    String[] row = content.get(1);
                    assertThat(row[0]).isEqualTo("Calgary");
                    assertThat(row[1]).isEqualTo("LW");
                    assertThat(row[2]).isEqualTo("ANDREW MANGIAPANE");

                }
            }
        }
    }

    @DisplayName("Jackson Core Test")
    @Test
    void jsonFileJackson () throws Exception {
    File file = new File("src/test/resources/HuberdeauNHL.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Player player = objectMapper.readValue(file, Player.class);
    assertThat(player.firstName).isEqualTo("Jonathan");
    assertThat(player.lastName).isEqualTo("Huberdeau");
    assertThat(player.age).isEqualTo(29);
    assertThat(player.isPlaying).isTrue();
    assertThat(player.teams.get(0)).isEqualTo("Florida");
    assertThat(player.teams.get(1)).isEqualTo("Calgary");
    assertThat(player.playingCareer.team).isEqualTo("Florida");
    assertThat(player.playingCareer.games).isEqualTo(671);
    assertThat(player.playingCareer.goals).isEqualTo(198);
    assertThat(player.playingCareer.contractUntil).isEqualTo("03.08.2022");
    }
}
