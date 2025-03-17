export default function fieldValidator(str:string,min:number,max:number): boolean {
    return str.length >= min && str.length <= max;
}
export function checkEmailFormat (email:string) : boolean {
    var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return re.test(email);
}