/**
 * DB-stored paths from asset seeding used "/assets/AppImages/..." which the dev server
 * does not serve. Real files are exposed at "/AppImages/..." via vue.config.js.
 */
export function resolveImageUrl(url) {
  if (url == null || typeof url !== "string") {
    return url;
  }
  if (url.startsWith("/assets/AppImages/")) {
    return url.replace("/assets/AppImages/", "/AppImages/");
  }
  // Older seeding relativized from src/assets and duplicated the AppImages segment
  if (url.includes("/AppImages/AppImages/")) {
    return url.replace("/AppImages/AppImages/", "/AppImages/");
  }
  return url;
}
