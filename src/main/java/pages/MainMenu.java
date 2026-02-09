package pages;

public enum MainMenu {
    // צד ימין: הטקסט המדויק בקישור | צד שמאל: מילה אחת שחייבת להופיע בכותרת הדף החדש
    MIZU_ZECHUOT("מיצוי זכויות", "זכויות"), // תיקנתי ל-זכויות
    KITZBAOT("קצבאות והטבות", "קצבאות"),
    DEMEI_BITUAH("דמי ביטוח", "דמי ביטוח"),
    YEZIRAT_KESHER("יצירת קשר", "יצירת קשר");

    private final String linkText;      // הטקסט עליו נלחץ
    private final String expectedTitle; // הטקסט שנבדוק בכותרת

    MainMenu(String linkText, String expectedTitle) {
        this.linkText = linkText;
        this.expectedTitle = expectedTitle;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }
}