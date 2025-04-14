package com.yutsuki.chatserver.common;

public class Regex {

    /**
     * รูปแบบ (Regular Expression) สำหรับตรวจสอบอีเมล
     * <p>ตัวอย่างอีเมลที่ถูกต้อง: <code>example@email.com</code></p>
     *
     * @see <a href="https://uibakery.io/regex-library/email-regex-java">Email regex Java</a>
     */
    public static final String EMAIL = "^\\S+@\\S+\\.\\S+$";

    /**
     * รูปแบบ (Regex) สำหรับตรวจสอบความถูกต้องของชื่อผู้ใช้ (username) ตามข้อกำหนดของ Instagram
     * <ul>
     *   <li><b>ต้องขึ้นต้น</b> ด้วยตัวอักษร (a-z, A-Z) หรือ ตัวเลข (0-9)</li>
     *   <li>สามารถมีตัวอักษร (a-z, A-Z), ตัวเลข (0-9), จุด (.) และขีดล่าง (_)</li>
     *   <li><b>ห้าม</b> มีจุด (.) ซ้ำกัน (เช่น <code>"user..name"</code> ❌)</li>
     *   <li><b>ห้าม</b> ลงท้ายด้วยจุด (เช่น <code>"username."</code> ❌)</li>
     *   <li>ความยาวรวมต้องอยู่ระหว่าง <b>1-30 ตัวอักษร</b></li>
     *   <li>ต้องเป็นอักขระ ASCII เท่านั้น (ไม่รองรับภาษาไทยหรืออักขระพิเศษอื่น ๆ)</li>
     * </ul>
     *
     * <p><b>ตัวอย่างที่ถูกต้อง:</b></p>
     * <ul>
     *   <li><code>"username123"</code> ✅</li>
     *   <li><code>"user.name"</code> ✅</li>
     *   <li><code>"valid_user_1"</code> ✅</li>
     * </ul>
     *
     * <p><b>ตัวอย่างที่ไม่ถูกต้อง:</b></p>
     * <ul>
     *   <li><code>"_username"</code> ❌ (ขึ้นต้นด้วย _)</li>
     *   <li><code>".username"</code> ❌ (ขึ้นต้นด้วย .)</li>
     *   <li><code>"user..name"</code> ❌ (มี ..)</li>
     *   <li><code>"username."</code> ❌ (ลงท้ายด้วย .)</li>
     *   <li><code>"way_too_long_username_over_30_characters"</code> ❌ (เกิน 30 ตัวอักษร)</li>
     * </ul>
     */
    public static final String USERNAME = "^(?!.*\\.\\.)(?!.*\\.$)[a-zA-Z0-9][a-zA-Z0-9._]{0,29}$";

    /**
     * รูปแบบ (Regular Expression) สำหรับตรวจสอบรหัสผ่าน
     * <p>
     * เงื่อนไข:
     * <ul>
     *     <li>ต้องมีความยาวอย่างน้อย 8 ตัวอักษร และไม่เกิน 30 ตัวอักษร สามารถปรับได้โดยแก้ไข <code>{8,30}</code></li>
     *     <li>สามารถใช้ตัวอักษรภาษาอังกฤษพิมพ์ใหญ่ (A-Z), ตัวพิมพ์เล็ก (a-z), ตัวเลข (0-9) และอักขระพิเศษ <code>#?!@$%^&*-</code></li>
     *     <li>ไม่บังคับให้ต้องมีตัวพิมพ์ใหญ่, ตัวพิมพ์เล็ก, ตัวเลข หรืออักขระพิเศษ</li>
     * </ul>
     * </p>
     *
     * <p><b>ตัวอย่างที่ถูกต้อง:</b></p>
     * <ul>
     *     <li><code>Password1!</code></li>
     *     <li><code>testonlypassword</code></li>
     *     <li><code>12345678</code></li>
     *     <li><code>!@#pass12</code></li>
     *     <li><code>OnlySpecial@#</code></li>
     * </ul>
     *
     * <p><b>ตัวอย่างที่ไม่ถูกต้อง:</b></p>
     * <ul>
     *     <li><code>short1!</code> (น้อยกว่า 8 ตัวอักษร)</li>
     *     <li><code>thispasswordiswaytoolongandshouldnotbevalid</code> (เกิน 30 ตัวอักษร)</li>
     * </ul>
     *
     * @see <a href="https://uibakery.io/regex-library/password-regex-java">Password regex Java</a>
     */
    public static final String PASSWORD = "^[A-Za-z0-9#?!@$%^&*-]{8,30}$";

}