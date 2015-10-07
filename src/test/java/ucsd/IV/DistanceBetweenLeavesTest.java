package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;
import ucsd.III.DistanceBetweenLeaves;

public class DistanceBetweenLeavesTest {
    @Test
    public void test() {
        String result = DistanceBetweenLeaves.doWork("src/test/resources/IV/sample/DistanceBetweenLeaves.txt");
        String expected =
                "0 13 21 22\n" +
                "13 0 12 13\n" +
                "21 12 0 13\n" +
                "22 13 13 0";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = DistanceBetweenLeaves.doWork("src/test/resources/IV/sample/DistanceBetweenLeavesExtra.txt");
        String expected =
                "0 70 186 161 201 44 33 86 185 101 89 41 99 129 66 50 71 96 78 67 153 142 209 57 112 106 105 167 53 125 41 209\n" +
                "70 0 226 201 241 84 73 40 225 55 129 81 139 169 106 44 111 136 32 107 193 182 249 33 152 60 59 207 93 165 51 249\n" +
                "186 226 0 45 27 152 177 242 25 257 123 169 97 73 148 206 127 108 234 139 53 56 35 213 88 262 261 31 151 91 197 35\n" +
                "161 201 45 0 60 127 152 217 44 232 98 144 72 48 123 181 102 83 209 114 28 31 68 188 63 237 236 26 126 66 172 68\n" +
                "201 241 27 60 0 167 192 257 40 272 138 184 112 88 163 221 142 123 249 154 68 71 22 228 103 277 276 46 166 106 212 22\n" +
                "44 84 152 127 167 0 35 100 151 115 55 27 65 95 32 64 37 62 92 33 119 108 175 71 78 120 119 133 19 91 55 175\n" +
                "33 73 177 152 192 35 0 89 176 104 80 32 90 120 57 53 62 87 81 58 144 133 200 60 103 109 108 158 44 116 44 200\n" +
                "86 40 242 217 257 100 89 0 241 27 145 97 155 185 122 60 127 152 30 123 209 198 265 49 168 32 31 223 109 181 67 265\n" +
                "185 225 25 44 40 151 176 241 0 256 122 168 96 72 147 205 126 107 233 138 52 55 48 212 87 261 260 30 150 90 196 48\n" +
                "101 55 257 232 272 115 104 27 256 0 160 112 170 200 137 75 142 167 45 138 224 213 280 64 183 35 34 238 124 196 82 280\n" +
                "89 129 123 98 138 55 80 145 122 160 0 72 36 66 51 109 30 33 137 42 90 79 146 116 49 165 164 104 54 62 100 146\n" +
                "41 81 169 144 184 27 32 97 168 112 72 0 82 112 49 61 54 79 89 50 136 125 192 68 95 117 116 150 36 108 52 192\n" +
                "99 139 97 72 112 65 90 155 96 170 36 82 0 40 61 119 40 21 147 52 64 53 120 126 23 175 174 78 64 36 110 120\n" +
                "129 169 73 48 88 95 120 185 72 200 66 112 40 0 91 149 70 51 177 82 40 29 96 156 31 205 204 54 94 34 140 96\n" +
                "66 106 148 123 163 32 57 122 147 137 51 49 61 91 0 86 33 58 114 29 115 104 171 93 74 142 141 129 31 87 77 171\n" +
                "50 44 206 181 221 64 53 60 205 75 109 61 119 149 86 0 91 116 52 87 173 162 229 31 132 80 79 187 73 145 31 229\n" +
                "71 111 127 102 142 37 62 127 126 142 30 54 40 70 33 91 0 37 119 24 94 83 150 98 53 147 146 108 36 66 82 150\n" +
                "96 136 108 83 123 62 87 152 107 167 33 79 21 51 58 116 37 0 144 49 75 64 131 123 34 172 171 89 61 47 107 131\n" +
                "78 32 234 209 249 92 81 30 233 45 137 89 147 177 114 52 119 144 0 115 201 190 257 41 160 50 49 215 101 173 59 257\n" +
                "67 107 139 114 154 33 58 123 138 138 42 50 52 82 29 87 24 49 115 0 106 95 162 94 65 143 142 120 32 78 78 162\n" +
                "153 193 53 28 68 119 144 209 52 224 90 136 64 40 115 173 94 75 201 106 0 23 76 180 55 229 228 34 118 58 164 76\n" +
                "142 182 56 31 71 108 133 198 55 213 79 125 53 29 104 162 83 64 190 95 23 0 79 169 44 218 217 37 107 47 153 79\n" +
                "209 249 35 68 22 175 200 265 48 280 146 192 120 96 171 229 150 131 257 162 76 79 0 236 111 285 284 54 174 114 220 16\n" +
                "57 33 213 188 228 71 60 49 212 64 116 68 126 156 93 31 98 123 41 94 180 169 236 0 139 69 68 194 80 152 38 236\n" +
                "112 152 88 63 103 78 103 168 87 183 49 95 23 31 74 132 53 34 160 65 55 44 111 139 0 188 187 69 77 27 123 111\n" +
                "106 60 262 237 277 120 109 32 261 35 165 117 175 205 142 80 147 172 50 143 229 218 285 69 188 0 11 243 129 201 87 285\n" +
                "105 59 261 236 276 119 108 31 260 34 164 116 174 204 141 79 146 171 49 142 228 217 284 68 187 11 0 242 128 200 86 284\n" +
                "167 207 31 26 46 133 158 223 30 238 104 150 78 54 129 187 108 89 215 120 34 37 54 194 69 243 242 0 132 72 178 54\n" +
                "53 93 151 126 166 19 44 109 150 124 54 36 64 94 31 73 36 61 101 32 118 107 174 80 77 129 128 132 0 90 64 174\n" +
                "125 165 91 66 106 91 116 181 90 196 62 108 36 34 87 145 66 47 173 78 58 47 114 152 27 201 200 72 90 0 136 114\n" +
                "41 51 197 172 212 55 44 67 196 82 100 52 110 140 77 31 82 107 59 78 164 153 220 38 123 87 86 178 64 136 0 220\n" +
                "209 249 35 68 22 175 200 265 48 280 146 192 120 96 171 229 150 131 257 162 76 79 16 236 111 285 284 54 174 114 220 0";
        Assert.assertEquals(result, expected);
    }
}

