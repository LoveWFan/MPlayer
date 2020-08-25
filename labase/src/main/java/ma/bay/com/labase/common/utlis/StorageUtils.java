package ma.bay.com.labase.common.utlis;

import android.os.Environment;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by wangl2138 on 2017/11/21.
 */

public class StorageUtils {
	private static final String TAG = "StorageUtils";

	public static final int TYPE_AUDIO = 0x01;
	public static final int TYPE_VIDEO = 0x02;
	public static final int TYPE_IMAGE = 0X04;
	public static final int TYPE_DATA = 0x08;
	public static final int TYPE_RECORD = 0x10;
	public static final int TYPE_CACHE = 0x100;

	private static final String LA_BASE_DIR_NAME = "LA";

	@IntDef({TYPE_AUDIO, TYPE_IMAGE, TYPE_VIDEO, TYPE_DATA, TYPE_RECORD, TYPE_CACHE})
	public @interface Type {

	}

	public static String getDir(@Type int type) {
		return getDir(type, null);
	}

	public static String getDir(@Type int type, @Nullable String bizName) {
		return getDir(type, bizName, null);
	}

	public static String getDir(@Type int type, @Nullable String bizName, String userId) {
		String path = TextUtils.isEmpty(bizName) ?
				String.format(
						"%s/%s/%s/%s/%s",
						Environment.getExternalStorageDirectory(),
						LA_BASE_DIR_NAME,
						"fairy",
						userId2Path(userId),
						type2Path(type)
				) :
				String.format(
						"%s/%s/%s/%s/%s/%s",
						Environment.getExternalStorageDirectory(),
						LA_BASE_DIR_NAME,
						"fairy",
						userId2Path(userId),
						type2Path(type),
						bizName2Path(bizName)
				);

		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				i("make public directories failed: " + path);
			}
		}

		return path;
	}

	public static String getGuestDir(@Type int type, @Nullable String bizName) {
		String path = TextUtils.isEmpty(bizName) ?
				String.format(
						"%s/%s/%s/%s/%s",
						Environment.getExternalStorageDirectory(),
						LA_BASE_DIR_NAME,
						"fairy",
						getGuestPath(),
						type2Path(type)
				) :
				String.format(
						"%s/%s/%s/%s/%s/%s",
						Environment.getExternalStorageDirectory(),
						LA_BASE_DIR_NAME,
						"fairy",
						getGuestPath(),
						type2Path(type),
						bizName2Path(bizName)
				);

		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				i("make public directories failed: " + path);
			}
		}

		return path;
	}

	private static String type2Path(@Type int type) {
		if (type == TYPE_AUDIO) {
			return "audio";
		}

		if (type == TYPE_IMAGE) {
			return "image";
		}

		if (type == TYPE_DATA) {
			return "data";
		}

		if (type == TYPE_RECORD) {
			return "record";
		}

		if (type == TYPE_CACHE) {
			return "cache";
		}

		return "video";
	}

	private static String bizName2Path(String bizName) {
		if (TextUtils.isEmpty(bizName)) {
			return "";
		}

		return "biz_" + bizName;
	}

	private static String userId2Path(String userId) {
		if (TextUtils.isEmpty(userId)) {
			return "public";
		}

		return "u_" + userId;
	}

	private static String getGuestPath() {
		return "guest";
	}

	private static void i(String message) {
		Log.i(TAG, message);
	}
}
