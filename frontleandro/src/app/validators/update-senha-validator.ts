import { AbstractControl, ValidatorFn, Validators, ValidationErrors } from '@angular/forms';

export function validarSenhaUpdate(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        // Verificar se o valor do campo senha é vazio (ou seja, não foi modificado pelo usuário)
        const senhaAtual = control.value;
        if (senhaAtual === '') {
            return null; // Se estiver vazio, não aplicar validação
        }

        // Se a senha foi fornecida, aplicar validador de padrão
        return Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[-[!“#$%&'()*+,-./:;<=>?@[\]^_`{|}~]+).{6,10}$/)(control); // Substitua 'sua_regex_aqui' pelo padrão de senha desejado
    };
}
