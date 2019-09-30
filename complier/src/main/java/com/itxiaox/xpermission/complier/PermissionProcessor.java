package com.itxiaox.xpermission.complier;

import com.google.auto.service.AutoService;
import com.itxiaox.xpermission.annotation.NeedsPermission;
import com.itxiaox.xpermission.annotation.OnNeverAskAgain;
import com.itxiaox.xpermission.annotation.OnPermissionDenied;
import com.itxiaox.xpermission.annotation.OnShowRationale;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//通过auto-service 中的@AutoService 可以自动生成AutoService注解处理器是Google开发的。
//用来生成META-INF/service/javax.annotation.processing.Processor文件
@AutoService(Processor.class) //通过google auto-service 来自动触发扫描生成java文件
public class PermissionProcessor extends AbstractProcessor {

    private Messager messager;//用来报告错误，警告，提示
    private Elements elementsUtils;//包含了很多的操作Elements的工具方法
    private Filer filer; // 用来创建按新的源文件, class文件, 创建源文件的核心方法，
    private Types typesUtils; //包含了用于操作TypeMIrror 工具方法

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typesUtils = processingEnv.getTypeUtils();
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    /**
     * 获取支持注解的类型
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(NeedsPermission.class.getCanonicalName());
        types.add(OnPermissionDenied.class.getCanonicalName());
        types.add(OnShowRationale.class.getCanonicalName());
        types.add(OnNeverAskAgain.class.getCanonicalName());
        return types;
    }

    /**
     * 返回注解支持的最新源版本，JDK版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        //@SupportedSourceVersion(SourceVersion.RELEASE_8); 可以在文件上，使用注解的方式代替次方法。
        return SourceVersion.latest();
    }

    /**
     * 注解处理器的核心方法，处理具体的注解实现，生成java文件
     *
     * @param annotations
     * @param roundEnv    拿到的是结构语言的标签组
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取MainActivity中所有带NeedsPermission注解的方法
        Map<String, List<ExecutableElement>>  needsPermissionMap =  getAnnotation(roundEnv,NeedsPermission.class);
        Map<String, List<ExecutableElement>>  onNeverAskAgainMap =  getAnnotation(roundEnv, OnNeverAskAgain.class);
        Map<String, List<ExecutableElement>>  onPermissionDenied =  getAnnotation(roundEnv,OnPermissionDenied.class);
        Map<String, List<ExecutableElement>>  onShowRationale =  getAnnotation(roundEnv,OnShowRationale.class);

        printMsg("process 开始生成类了");

        //*********开始生成类了******

        for (String activityName:needsPermissionMap.keySet()) {
            //获取“com.itxiaox.xpermission.MainActivity”中所有控件方法的集合
            List<ExecutableElement> needsPermissionElements = needsPermissionMap.get(activityName);
            List<ExecutableElement> onNeverAskAgainElements = onNeverAskAgainMap.get(activityName);
            List<ExecutableElement>  onPermissionDeniedElements = onPermissionDenied.get(activityName);
            List<ExecutableElement> onShowRationaleElements = onShowRationale.get(activityName);
            final String CLASS_SUFFIX = "$Permission";
            //创建一个新的源文件（Class）,并返回一个对象以允许写入它

            printMsg("activityName="+activityName);
            try {
                Filer filer = processingEnv.getFiler();
                JavaFileObject javaFileObject = filer.createSourceFile(activityName + CLASS_SUFFIX);
                //通过方法标签获取包名标签，（任意一个属性标签的父节点都是同一个包名）
                String packageName = getPackageName(needsPermissionElements.get(0));
                printMsg("packageName="+activityName);

                //定义Writer对象，开启真正的一行一行的来写代码
                Writer writer = javaFileObject.openWriter();
                //类名：MainActivity$Permission,不是com.itxiaox.permission.Mainactivity$Permissions
                String activitySimpleName = needsPermissionElements.get(0).getEnclosingElement().getSimpleName().toString() + CLASS_SUFFIX;

                //生成包
                writer.write("package "+ packageName +";\n");
                printMsg("package "+ packageName +";\n");
                //生成要导入的接口类（必须手动导入）
                writer.write("import com.itxiaox.xpermission.library.listener.PermissionRequest;\n");
                printMsg("import com.itxiaox.xpermission.library.listener.PermissionRequest;\n");

                writer.write("import com.itxiaox.xpermission.library.listener.RequestPermission;\n");
                printMsg("import com.itxiaox.xpermission.library.listener.RequestPermission;\n");

                writer.write("import com.itxiaox.xpermission.library.utils.PermissionUtils;\n");
                printMsg("import com.itxiaox.xpermission.library.utils.PermissionUtils;\n");

                writer.write("import androidx.appcompat.app.AppCompatActivity;\n");
//                writer.write("import android.support.annotation.NonNull;\n");
                printMsg("import androidx.appcompat.app.AppCompatActivity;\n");

                writer.write("import androidx.core.app.ActivityCompat;\n");
                printMsg("import androidx.core.app.ActivityCompat;\n");

                writer.write("import java.lang.ref.WeakReference;\n");
                printMsg("import java.lang.ref.WeakReference;\n");
                //生成类
                writer.write("public class "+ activitySimpleName + " implements RequestPermission<"+activityName+"> {\n");
                printMsg("public class "+ activitySimpleName + " implements RequestPermission<"+activityName+"> {\n");

                //生成常量属性
                writer.write("private static final int REQUEST_SHOWCAMERA = 666;\n");
                printMsg("private static final int REQUEST_SHOWCAMERA = 666;\n");
                writer.write("private static String[] PERMISSION_SHOWCAMERA;\n");
                printMsg("private static String[] PERMISSION_SHOWCAMERA;\n");

                //生成requestPermission方法
                writer.write("public void requestPermission(" + activityName + " target,String[] permissions){\n");
                printMsg("public void requestPermission(" + activityName + " target,String[] permissions){\n");

                writer.write("PERMISSION_SHOWCAMERA =  permissions;\n");
                printMsg("PERMISSION_SHOWCAMERA =  permissions;\n");
                writer.write("if  (PermissionUtils.hasSelfPermissions(target,PERMISSION_SHOWCAMERA)){\n");
                printMsg("if  (PermissionUtils.hasSelfPermissions(target,PERMISSION_SHOWCAMERA)){\n");
                //循环生成MainActivity每个权限申请方法
                for (ExecutableElement executableElement : needsPermissionElements) {
                    //获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    //调用申请权限方法
                    writer.write("target."+methodName+"();\n");
                    printMsg("target."+methodName+"();\n");
                }
                writer.write("} else if(PermissionUtils.shouldShowRequestPermissionRationale(target,PERMISSION_SHOWCAMERA)){\n");
                printMsg("} else if(PermissionUtils.shouldShowRequestPermissionRationale(target,PERMISSION_SHOWCAMERA)){\n");
                //循环生成Mainactiviy每个提示用户为何要开启权限的方法
                if (onShowRationaleElements !=null && !onShowRationaleElements.isEmpty()){
                    for (ExecutableElement executableElement : onShowRationaleElements) {
                        //获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        //调用提示用户为何要开启权限方法
                        writer.write("target."+methodName+"(new PermissionRequestImpl(target));\n");

                        printMsg("target."+methodName+"(new PermissionRequestImpl(target));\n");
                    }
                }

                writer.write("} else {\n");
                printMsg("} else {\n");
                writer.write("ActivityCompat.requestPermissions(target,PERMISSION_SHOWCAMERA,REQUEST_SHOWCAMERA);\n}\n}\n");
                printMsg("ActivityCompat.requestPermissions(target,PERMISSION_SHOWCAMERA,REQUEST_SHOWCAMERA);\n}\n}\n");
                //生成onRequestPermissionsResult方法
                writer.write("public void onRequestPermissionsResult("+ activityName +" target,int requestCode,int[] grantResults ){\n");
                printMsg("public void onRequestPermissionsResult("+ activityName +" target,int requestCode,int[] grantResults ){\n");
                writer.write("switch(requestCode) {\n");
                printMsg("switch(requestCode) {\n");
                writer.write("case REQUEST_SHOWCAMERA:\n");
                printMsg("case REQUEST_SHOWCAMERA:\n");
                writer.write("if (PermissionUtils.verifyPermissions(grantResults)){\n");
                printMsg("if (PermissionUtils.verifyPermissions(grantResults)){\n");
                //循环生成MainActivity每个权限申请方法
                for (ExecutableElement executableElement : needsPermissionElements) {
                    //获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    //调用申请权限的方法
                    writer.write("target."+methodName+"();\n");
                    printMsg("target."+methodName+"();\n");
                }
                writer.write("} else if (!PermissionUtils.shouldShowRequestPermissionRationale(target,PERMISSION_SHOWCAMERA)){\n");
                printMsg("} else if (!PermissionUtils.shouldShowRequestPermissionRationale(target,PERMISSION_SHOWCAMERA)){\n");
                //循环生成MainActivity每个不在询问后的提示
                if (onNeverAskAgainElements !=null && !onNeverAskAgainElements.isEmpty()){
                    for (ExecutableElement executableElement : onNeverAskAgainElements) {
                        //获取方法名
                        String methodName = executableElement.getSimpleName().toString();

                        writer.write("target."+methodName+"();\n");
                        printMsg("target."+methodName+"();\n");
                    }
                }
                writer.write("} else {\n");
                printMsg("} else {\n");

                //循环生成MainActivity每个拒绝时的提示方法
                if(onPermissionDenied !=null && !onPermissionDeniedElements.isEmpty()){
                    for (ExecutableElement executableElement : onPermissionDeniedElements) {
                        //获取方法名
                        String method = executableElement.getSimpleName().toString();
                        //调用拒绝时的提示方法
                        writer.write("target."+method+"();\n");
                        printMsg("target."+method+"();\n");
                    }
                }
                writer.write("}\nbreak;\ndefault: \nbreak;\n}\n}\n");
                printMsg("}\nbreak;\ndefault: \nbreak;\n}\n}\n");

                //生成接口实现类：PermissionRequestImpl implements PermissionRequest
                writer.write("private static final class PermissionRequestImpl implements PermissionRequest {\n");
                printMsg("private static final class PermissionRequestImpl implements PermissionRequest {\n");

                writer.write("private final WeakReference<"+ activityName +"> weakTarget;\n");
                printMsg("private final WeakReference<"+ activityName +"> weakTarget;\n");

                writer.write("private PermissionRequestImpl("+activityName+" target){\n");
                printMsg("private PermissionRequestImpl("+activityName+" target){\n");

                writer.write("this.weakTarget = new WeakReference(target);\n}\n");
                printMsg("this.weakTarget = new WeakReference(target);\n}\n");

                writer.write("public void proceed(){\n");
                printMsg("public void proceed(){\n");

                writer.write(activityName+" target = ("+activityName+") this.weakTarget.get();\n");
                printMsg(activityName+" target = ("+activityName+") this.weakTarget.get();\n");

                writer.write("if (target != null ) {\n");
                printMsg("if (target != null ) {\n");


                writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA,REQUEST_SHOWCAMERA);" +
                        "\n}\n}\n}\n");
                printMsg("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA,REQUEST_SHOWCAMERA);" +
                        "\n}\n}\n}\n");
                writer.write("\n}");
                printMsg("\n}");
                //最后结束标签，代码生成完成。
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private  Map<String, List<ExecutableElement>>  getAnnotation(RoundEnvironment roundEnv,Class<? extends Annotation> clazz) {
        Set<? extends Element> needsPermissionSet = roundEnv.getElementsAnnotatedWith(clazz);
        //保存起来，键值对，key: com.xxx.MainActivity value:所有带NeedsPermission注解的方法
        Map<String, List<ExecutableElement>> needsPermissionMap = new HashMap<>();
        //遍历所有带NeedsPermission注解的方法
        for (Element element : needsPermissionSet) {
            //转换成方法元素（结构体元素）
            ExecutableElement executableElement = (ExecutableElement) element;
            //通过方法元素获取它所属的MainActivity类名，如：com.itxiaox.xpermission.Maintivity
            String activityName = getActivityName(executableElement);
            //从缓存集合中获取MainActivity所有带NeedsPermission注解的方法集合
            List<ExecutableElement> list = needsPermissionMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                //先加入map集合，引用变量list可以动态的改变值
                needsPermissionMap.put(activityName, list);
            }
            //将MainActivity中所有带NeedsPermission注解的方法加入到List集合中
            list.add(executableElement);
        }
        return  needsPermissionMap;
    }


    private String getActivityName(ExecutableElement executableElement) {
        //思路：通过方法标签获取类名标签，再通过类名标签获取包名标签
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();//通过方法的标签，获取上一级，就是类
        //通过类名标签获取包名标签
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        //完成字符串拼接：com.itxiaox.permission + "."+MainActivity
        return packageName + "." + typeElement.getSimpleName().toString();
    }


    private String getPackageName(ExecutableElement executableElement){
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();//通过方法的标签，获取上一级，就是类
        //通过类名标签获取包名标签
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        return  packageName;
    }

    private void printMsg(String msg){
        messager.printMessage(Diagnostic.Kind.NOTE,msg);
    }
}