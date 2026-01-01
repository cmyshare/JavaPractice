package com.open.tree;

import cn.hutool.core.util.IdUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TreeUtil工具包方法
 * 工具类下的方法都需要使用简单健壮的泛型+函数式接口来接收入参和出参。
 *
 * 常见的函数式接口
 * Java 8自带了一些常用的函数式接口，位于java.util.function包中：
 * Function<T, R>：接受一个参数，返回一个结果。
 * Consumer<T>：接受一个参数，没有返回值。
 * Supplier<T>：不接受参数，返回一个结果。
 * Predicate<T>：接受一个参数，返回一个boolean值。
 * UnaryOperator<T>：接受和返回同类型的对象。
 * BinaryOperator<T>：接受两个相同类型的参数并返回相同类型的结果。
 *
 * 1.标准化接口减少了开发者自己定义类似接口的需要，并且提高了代码的可移植性和复用性。
 * 2.提高代码的功能扩展性，通过组合不同的函数式接口，可以轻松地扩展和修改功能，而无需对现有的代码进行大量改动。
 * 3.函数式接口为API设计提供了更灵活的选项。API用户可以通过传递Lambda表达式或方法引用来自定义行为，使API更加通用和实用。
 *
 * 参考链接：
 * https://mp.weixin.qq.com/s?__biz=MzIzOTU0NTQ0MA==&mid=2247544075&idx=1&sn=81dfddf5a91e22a0f27c608947531dcf&poc_token=HOxBVmmjGQ3NnvWBuQ28JkhMS37-nGS4VctsPaTa
 * https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247573042&idx=1&sn=333941fc930c1fc999d1b009ee0e425a&chksm=fa4ba183cd3c289563abd4d191b5d4e2fd77992fa8ebe04022f696b3f7781080c0aa1a81fbf4&mpshare=1&scene=24&srcid=0521rer0Tb6jbufqmzpYJAok&sharer_sharetime=1684640917601&sharer_shareid=4b447a3cb6ab5d3443a5fc9771951705#rd
 * https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247609935&idx=2&sn=bb4971bb955d3e6d9873252c9665744b&chksm=fb5706df712f7fcffaae201a37f5fc5eabc405002c90b5d44c7eeeac1c063f2e28fad52605e0&mpshare=1&scene=24&srcid=0802Ry7fOfs9CWffQz9lNXMI&sharer_shareinfo=81ffc6644e36464cbcd036ac050aeff8&sharer_shareinfo_first=81ffc6644e36464cbcd036ac050aeff8#rd
 * https://mp.weixin.qq.com/s?__biz=MzU1MzczMjg3MQ==&mid=2247484357&idx=1&sn=4e0ae99c1cdabdfe3ad6b85f46cf8e72&chksm=fbef1dadcc9894bb0011f3b4922bff7b2c1183f634be66edf0db29b18de8848f0342df51b62a&mpshare=1&scene=24&srcid=0729rEDj5KlqceCv0XvZOwkN&sharer_shareinfo=218a0e648b5d7dfb412e993d3afeacd5&sharer_shareinfo_first=218a0e648b5d7dfb412e993d3afeacd5#rd
 * https://mp.weixin.qq.com/s?__biz=MzU3MDAzNDg1MA==&mid=2247539547&idx=1&sn=18c6bf96509111f604b4eb4978d929c2&chksm=fd7c8f2ddf3f9b02b9d00d5f1eddc04b94a3e33656ee1c59bbe8c22fc692114d219647132fc0&mpshare=1&scene=24&srcid=1215dXifTDw3gm3cw1Xl5Lg8&sharer_shareinfo=c22ed3194055d6c9218edcd988a75654&sharer_shareinfo_first=c22ed3194055d6c9218edcd988a75654#rd
 */
public class AliTreeUtil {
    /**
     * 构建树
     *
     * @param menuList       需要合成树的List
     * @param pIdGetter      对象中获取父级ID方法,如:Menu:getParentId
     * @param idGetter       对象中获取主键ID方法 ,如：Menu:getId
     * @param rootCheck      判断对象是否根节点的方法，如： x->x.getParentId()==null,x->x.getParentMenuId()==0
     * @param setSubChildren 对象中设置下级数据列表方法，如： Menu::setSubMenus
     */
    public static <T, E> List<E> makeTree(List<E> menuList, Function<E, T> pIdGetter, Function<E, T> idGetter, Predicate<E> rootCheck, BiConsumer<E, List<E>> setSubChildren) {
        Map<T, List<E>> parentMenuMap = new HashMap<>();
        for (E node : menuList) {
            //获取父节点id
            T parentId = pIdGetter.apply(node);
            //节点放入父节点的value内
            parentMenuMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
        }
        List<E> result = new ArrayList<>();
        for (E node : menuList) {
            //添加到下级数据中
            setSubChildren.accept(node, parentMenuMap.getOrDefault(idGetter.apply(node), new ArrayList<>()));
            //如里是根节点，加入结构
            if (rootCheck.test(node)) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 将树形结构展平为一维列表（DFS深度优先遍历）
     * 极端宽树（根节点有 100,000 个子节点，使用DFS避免内存spike）
     * 普通菜单树（深度 ≤ 10，每层 ≤ 20 节点），推荐DFS
     *
     * @param tree           树的根节点列表
     * @param getSubChildren 获取子节点列表的方法，如：Menu::getSubMenus
     * @param <E>            节点类型
     * @return 扁平化的列表，包含所有节点（包括根和子孙）
     */
    public static <E> List<E> flattenTreeDFS(List<E> tree, Function<E, List<E>> getSubChildren) {
        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) {
            return result;
        }

        for (E node : tree) {
            result.add(node); // 先加入当前节点
            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                // 递归展平子树，并添加到结果中
                result.addAll(flattenTreeDFS(children, getSubChildren));
            }
        }
        return result;
    }

    /**
     * 将树形结构展平为一维列表（BFS广度优先遍历）
     * 极端深树（深度 = 10,000，链式结构），使用BFS避免StackOverflowError，也可用DFS 改为 显式栈（非递归） 可避免栈溢出。
     *
     * @param tree           树的根节点列表（通常为顶层根节点集合）
     * @param getSubChildren 获取子节点列表的方法，如：Menu::getSubMenus
     * @param <E>            节点类型
     * @return 按层序遍历的扁平化列表
     */
    public static <E> List<E> flattenTreeBFS(List<E> tree, Function<E, List<E>> getSubChildren) {
        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) {
            return result;
        }

        Queue<E> queue = new LinkedList<>(tree); // 初始化队列，加入所有根节点

        while (!queue.isEmpty()) {
            E node = queue.poll(); // 取出队首节点
            result.add(node);      // 加入结果

            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children); // 将子节点加入队尾
            }
        }

        return result;
    }

    /**
     * 将树形结构展平为一维列表（DFS深度优先遍历，非递归，显式栈）
     * 避免递归深度过大导致 StackOverflowError，适用于极深树。
     *
     * @param tree 树的根节点列表
     * @return 扁平化列表（DFS 顺序）
     */
    public static <E> List<E> flattenTreeDFSIterative(List<E> tree, Function<E, List<E>> getSubChildren) {
        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) {
            return result;
        }

        Deque<E> stack = new ArrayDeque<>();
        // 逆序压入，保证从左到右遍历（与递归一致）
        for (int i = tree.size() - 1; i >= 0; i--) {
            stack.push(tree.get(i));
        }

        while (!stack.isEmpty()) {
            E node = stack.pop();
            result.add(node);
            List<E> children = getSubChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                // 子节点也要逆序压栈，才能保持左→右访问顺序
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        }
        return result;
    }


    /**
     * 通用 DFS（递归）+ 设置 depth & ancestors
     *
     * @param tree            树的根节点列表
     * @param getSubChildren  获取子节点的方法
     * @param getId           获取节点 ID 的方法（用于构建 ancestors）
     * @param setDepth        设置 depth 的方法
     * @param setAncestors    设置 ancestors 的方法
     * @param <E>             节点类型
     * @param <ID>            ID 类型
     * @return 扁平化列表
     */
    public static <E, ID> List<E> flattenTreeDFSWithDepthAndAncestors(
            List<E> tree,
            Function<E, List<E>> getSubChildren,
            Function<E, ID> getId,
            BiConsumer<E, Integer> setDepth,
            BiConsumer<E, String> setAncestors) {

        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) return result;

        for (E node : tree) {
            dfsFill(node, "0", 1, getSubChildren, getId, setDepth, setAncestors, result);
        }
        return result;
    }

    /**
     * 递归填充树节点的 depth 和 ancestors
     * @param node
     * @param parentAncestors
     * @param depth
     * @param getSubChildren
     * @param getId
     * @param setDepth
     * @param setAncestors
     * @param result
     * @param <E>
     * @param <ID>
     */
    private static <E, ID> void dfsFill(
            E node,
            String parentAncestors,
            int depth,
            Function<E, List<E>> getSubChildren,
            Function<E, ID> getId,
            BiConsumer<E, Integer> setDepth,
            BiConsumer<E, String> setAncestors,
            List<E> result) {

        // 设置当前节点
        setDepth.accept(node, depth);
        setAncestors.accept(node, parentAncestors);
        result.add(node);

        List<E> children = getSubChildren.apply(node);
        if (children != null && !children.isEmpty()) {
            String currentAncestors = parentAncestors + "," + getId.apply(node);
            for (E child : children) {
                dfsFill(child, currentAncestors, depth + 1, getSubChildren, getId, setDepth, setAncestors, result);
            }
        }
    }

    // 可选：用于 BFS/DFS-Iterative 中传递状态
    private static final class NodeState<E> {
        final E node;
        final String ancestors;
        final int depth;

        NodeState(E node, String ancestors, int depth) {
            this.node = node;
            this.ancestors = ancestors;
            this.depth = depth;
        }
    }

    /**
     * 通用 BFS + 设置 depth & ancestors
     *
     * @param tree            树的根节点列表
     * @param getSubChildren  获取子节点的方法
     * @param getId           获取节点 ID 的方法（用于构建 ancestors）
     * @param setDepth        设置 depth 的方法
     * @param setAncestors    设置 ancestors 的方法
     * @param <E>             节点类型
     * @param <ID>            ID 类型
     * @return 扁平化列表
     */
    public static <E, ID> List<E> flattenTreeBFSWithDepthAndAncestors(
            List<E> tree,
            Function<E, List<E>> getSubChildren,
            Function<E, ID> getId,
            BiConsumer<E, Integer> setDepth,
            BiConsumer<E, String> setAncestors) {

        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) return result;

        Queue<NodeState<E>> queue = new LinkedList<>();
        for (E root : tree) {
            setDepth.accept(root, 1);
            setAncestors.accept(root, "0");
            result.add(root);
            queue.offer(new NodeState<>(root, "0", 1));
        }

        while (!queue.isEmpty()) {
            NodeState<E> current = queue.poll();
            List<E> children = getSubChildren.apply(current.node);
            if (children != null && !children.isEmpty()) {
                String currentAncestors = current.ancestors + "," + getId.apply(current.node);
                int nextDepth = current.depth + 1;
                for (E child : children) {
                    setDepth.accept(child, nextDepth);
                    setAncestors.accept(child, currentAncestors);
                    result.add(child);
                    queue.offer(new NodeState<>(child, currentAncestors, nextDepth));
                }
            }
        }
        return result;
    }

    /**
     * 通用 DFS（非递归）+ 设置 depth & ancestors
     *
     * @param tree            树的根节点列表
     * @param getSubChildren  获取子节点的方法
     * @param getId           获取节点 ID 的方法（用于构建 ancestors）
     * @param setDepth        设置 depth 的方法
     * @param setAncestors    设置 ancestors 的方法
     * @param <E>             节点类型
     * @param <ID>            ID 类型
     * @return 扁平化列表
     */
    public static <E, ID> List<E> flattenTreeDFSIterativeWithDepthAndAncestors(
            List<E> tree,
            Function<E, List<E>> getSubChildren,
            Function<E, ID> getId,
            BiConsumer<E, Integer> setDepth,
            BiConsumer<E, String> setAncestors) {

        List<E> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) return result;

        Deque<NodeState<E>> stack = new ArrayDeque<>();
        // 逆序压栈
        for (int i = tree.size() - 1; i >= 0; i--) {
            E root = tree.get(i);
            setDepth.accept(root, 1);
            setAncestors.accept(root, "0");
            result.add(root);
            stack.push(new NodeState<>(root, "0", 1));
        }

        while (!stack.isEmpty()) {
            NodeState<E> current = stack.pop();
            List<E> children = getSubChildren.apply(current.node);
            if (children != null && !children.isEmpty()) {
                String currentAncestors = current.ancestors + "," + getId.apply(current.node);
                int nextDepth = current.depth + 1;
                // 逆序压入子节点
                for (int i = children.size() - 1; i >= 0; i--) {
                    E child = children.get(i);
                    setDepth.accept(child, nextDepth);
                    setAncestors.accept(child, currentAncestors);
                    result.add(child);
                    stack.push(new NodeState<>(child, currentAncestors, nextDepth));
                }
            }
        }
        return result;
    }


    /**
     * 树中查找（当前节点不匹配predicate，只要其子节点列表匹配到即保留）
     * @param tree  需要查找的树
     * @param predicate  过滤条件，根据业务场景自行修改
     * @param getSubChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @return List<E> 过滤后的树
     * @param <E> 泛型实体对象
     */
    public static <E> List<E> search(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getSubChildren) {
        List<E> result = new ArrayList<>();

        for (E item : tree) {
            List<E> childList = getSubChildren.apply(item);
            List<E> filteredChildren = new ArrayList<>();

            if (childList != null && !childList.isEmpty()) {
                filteredChildren = search(childList, predicate, getSubChildren);
            }
            // 如果当前节点匹配，或者至少有一个子节点保留，就保留当前节点
            if (predicate.test(item) || !filteredChildren.isEmpty()) {
                result.add(item);
                // 还原下一级子节点如果有
                if (!filteredChildren.isEmpty()) {
                    getSubChildren.apply(item).clear();
                    getSubChildren.apply(item).addAll(filteredChildren);
                }
            }
        }
        return result;
    }


    /**
     * 树中过滤
     * @param tree  需要进行过滤的树
     * @param predicate  过滤条件判断
     * @param getChildren 获取下级数据方法，如：Menu::getSubMenus
     * @return List<E> 过滤后的树
     * @param <E> 泛型实体对象
     */
    public static <E> List<E> filter(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren) {
        //filter()方法和Stream的filter()方法一样，过滤满足条件的数据节点，如里当前节点不满足其所有子节点都会过滤掉。
        return tree.stream().filter(item -> {
            if (predicate.test(item)) {
                List<E> children = getChildren.apply(item);
                if (children != null && !children.isEmpty()) {
                    filter(children, predicate, getChildren);
                }
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }



    /**
     * 对树形结构进行排序
     *
     * @param tree         要排序的树形结构，表示为节点列表。
     * @param comparator   用于节点比较的比较器。
     * @param getChildren  提供一种方法来获取每个节点的子节点列表。
     * @param <E>          元素的类型。
     * @return 排序后的节点列表。
     */
    public static <E> List<E> sort(List<E> tree, Comparator<? super E> comparator, Function<E, List<E>> getChildren) {
        // 对树的每个节点进行迭代处理
        for (E item : tree) {
            // 获取当前节点的子节点列表
            List<E> childList = getChildren.apply(item);
            // 如果子节点列表不为空，则递归调用 sort 方法对其进行排序
            if (childList != null && !childList.isEmpty()) {
                sort(childList, comparator, getChildren);
            }
        }
        // 对当前层级的节点列表进行排序
        // 这一步确保了所有直接子节点在递归返回后都已排序，然后对当前列表进行排序
        tree.sort(comparator);
        // 返回排序后的节点列表
        return tree;
    }


    /**
     * 树中过滤并进行节点处理（此处是将节点的choose字段置为false，那么就在页面上可以展示但无法被勾选）
     * @param tree        需要过滤的树
     * @param predicate   过滤条件
     * @param getChildren 获取下级数据方法，如：MenuVo::getSubMenus
     * @param setChoose   要被处理的字段，如：MenuVo::getChoose，可根据业务场景自行修改
     * @param <E>         泛型实体对象
     * @return List<E> 过滤后的树
     */
    public static <E> List<E> filterAndHandler(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren, BiConsumer<E, Boolean> setChoose) {
        //横向拓展，加入过滤或查找条件，即可完成一个通用树形过滤方法。再延伸，查找到匹配的节点后对此节点进行特殊业务需求处理也是使用频率极高的。
        return tree.stream().filter(item -> {
            //如果命中条件，则可以被勾选。（可根据业务场景自行修改）
            if (predicate.test(item)) {
                setChoose.accept(item, true);
            } else {
                setChoose.accept(item, false);
            }
            List<E> children = getChildren.apply(item);
            if (children != null && !children.isEmpty()) {
                filterAndHandler(children, predicate, getChildren, setChoose);
            }
            return true;
        }).collect(Collectors.toList());
    }


    /**
     * 树形结构再生工具类：基于展平列表，生成全新 ID、parentId、ancestors 的树形数据
     * @param originalFlat
     * @return
     */
    public static List<AliMenu> regenerateTreeWithNewLongIds(List<AliMenu> originalFlat) {
        Map<Long,Long> oldNewIdMap = new HashMap<>();
        // 先将所有节点的ID映射到新ID
        originalFlat.forEach(menu -> {
            // 生成新ID
            long newId = IdUtil.getSnowflake().nextId();
            oldNewIdMap.put(menu.getId(), newId);
            menu.setId(newId);
        });
        // 更新所有节点的parentId
        originalFlat.forEach(menu -> {
            Long newParentId = oldNewIdMap.get(menu.getParentId());
            menu.setParentId(newParentId);
        });
        // 更新所有节点的ancestors
        originalFlat.forEach(menu -> {
            // 分割旧的ancestors字符串
            String[] oldAncestors = menu.getAncestors().split(",");
            // 生成新的ancestors字符串
            String newAncestors = Arrays.stream(oldAncestors).map(m->{
                Long l = oldNewIdMap.get(Long.valueOf(m));
                return l == null ? "0" : String.valueOf(l);
            }).collect(Collectors.joining(","));
            menu.setAncestors(newAncestors);
        });
        return originalFlat;
    }


    /**
     * 通用方法：基于展平列表，生成新的 Long ID，并重置 parentId 和 ancestors
     *
     * @param flatList            原始展平列表（会直接修改原对象！）
     * @param getId               获取旧 ID
     * @param setId               设置新 ID
     * @param getParentId         获取旧 parentId
     * @param setParentId         设置新 parentId
     * @param getAncestors        获取旧 ancestors（如 "0,1,2"）
     * @param setAncestors        设置新 ancestors
     * @param newIdGenerator      新 ID 生成器（如 () -> IdUtil.getSnowflake().nextId()）
     * @param <E>                 节点类型
     * @return 修改后的原列表（same reference）
     */
    public static <E> List<E> regenerateTreeWithNewLongIds(
            List<E> flatList,
            Function<E, Long> getId,
            java.util.function.BiConsumer<E, Long> setId,
            Function<E, Long> getParentId,
            java.util.function.BiConsumer<E, Long> setParentId,
            Function<E, String> getAncestors,
            java.util.function.BiConsumer<E, String> setAncestors,
            java.util.function.Supplier<Long> newIdGenerator) {

        if (flatList == null || flatList.isEmpty()) {
            return flatList;
        }

        // 1. oldId -> newId 映射（注意：根节点的 parentId=0，也要映射？不，0 不在节点 ID 中）
        Map<Long, Long> oldToNewId = new HashMap<>();

        // 为每个节点生成新 ID
        for (E node : flatList) {
            Long oldId = getId.apply(node);
            Long newId = newIdGenerator.get();
            oldToNewId.put(oldId, newId);
            setId.accept(node, newId);
        }

        // 2. 更新 parentId（注意：parentId=0 表示根，0 不在 oldToNewId 中）
        for (E node : flatList) {
            Long oldParentId = getParentId.apply(node);
            // 如果是根节点（parentId == 0），则新 parentId 仍为 0
            Long newParentId = (oldParentId == null || oldParentId == 0L) ? 0L : oldToNewId.get(oldParentId);
            // 安全兜底：如果父ID不存在，设为0（避免NPE）
            if (newParentId == null) {
                newParentId = 0L;
            }
            setParentId.accept(node, newParentId);
        }

        // 3. 更新 ancestors
        for (E node : flatList) {
            String oldAncestors = getAncestors.apply(node);
            if (oldAncestors == null || oldAncestors.isEmpty()) {
                setAncestors.accept(node, "0");
                continue;
            }
            String newAncestors = Arrays.stream(oldAncestors.split(","))
                    .map(String::trim)
                    .map(part -> {
                        if ("0".equals(part)) {
                            return "0"; // 根前缀保持为 "0"
                        }
                        try {
                            Long oldId = Long.valueOf(part);
                            Long newId = oldToNewId.get(oldId);
                            return (newId != null) ? String.valueOf(newId) : "0";
                        } catch (NumberFormatException e) {
                            return "0"; // 非法ID默认为0
                        }
                    })
                    .collect(Collectors.joining(","));

            setAncestors.accept(node, newAncestors);
        }

        return flatList;
    }


}
