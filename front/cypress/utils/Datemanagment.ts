export default function getFormattedDate(addDays: number = 0): string {
    const now = new Date();
    now.setDate(now.getDate() + addDays);
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
  
    const hours = String(now.getHours()).padStart(2, "0");
    const minutes = String(now.getMinutes()).padStart(2, "0");
    const seconds = String(now.getSeconds()).padStart(2, "0");
  
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }
  export function getMonthName(dateString: string, locale: string = "en-US"): string {
    const [datePart] = dateString.split(" "); 
    const [year, month, day] = datePart.split("-").map(Number); 
    const date = new Date(year, month - 1, day); 
    return date.toLocaleString(locale, { month: "long" });
  }


  export function getYearFromDate(dateString: string): string {
    const [datePart] = dateString.split(" "); // Sépare la date de l'heure
    const [year] = datePart.split("-");       // Extrait l'année
    return year;
  }


  export function getDayFromDate(dateString: string): string {
    
    const [datePart] = dateString.split(" "); // Sépare la date de l'heure
    let[, , day] = datePart.split("-");  
    if(day[0]=="0")
      day= day.split("0")[1];
      // Extrait le jour
    return day;
  }
   